package com.fullcycle.catalogo.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fullcycle.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import com.fullcycle.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import com.fullcycle.catalogo.infrastructure.video.VideoClient;
import com.fullcycle.catalogo.infrastructure.video.models.VideoDTO;
import com.fullcycle.catalogo.infrastructure.video.models.VideoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoListener {

    private static final Logger LOG = LoggerFactory.getLogger(VideoListener.class);
    private static final TypeReference<MessageValue<VideoEvent>> VIDEO_MESSAGE_TYPE = new TypeReference<>() {
    };

    private final VideoClient videoClient;
    private final SaveVideoUseCase saveVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;

    public VideoListener(
            final VideoClient videoClient,
            final SaveVideoUseCase saveVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase
    ) {
        this.videoClient = Objects.requireNonNull(videoClient);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.videos.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.videos.topics}",
            groupId = "${kafka.consumers.videos.group-id}",
            id = "${kafka.consumers.videos.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.videos.auto-offset-reset}"
            }
    )
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumers.videos.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
        final var messagePayload = Json.readValue(payload, VIDEO_MESSAGE_TYPE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)) {
            this.deleteVideoUseCase.execute(new DeleteVideoUseCase.Input(messagePayload.before().id()));
        } else {
            this.videoClient.videoOfId(messagePayload.after().id())
                    .map(this::toUseCaseInput)
                    .ifPresentOrElse(this.saveVideoUseCase::execute, () -> {
                        LOG.warn("Genre was not found {}", messagePayload.after().id());
                    });
        }
    }

    private SaveVideoUseCase.Input toUseCaseInput(final VideoDTO dto) {
        return new SaveVideoUseCase.Input(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.yearLaunched(),
                dto.duration(),
                dto.rating(),
                dto.opened(),
                dto.published(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.video(),
                dto.trailer(),
                dto.banner(),
                dto.thumbnail(),
                dto.thumbnailHalf(),
                dto.categoriesId(),
                dto.castMembersId(),
                dto.genresId()
        );
    }

    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.warn("Message received from Kafka at DLT [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
    }
}
