package com.fullcycle.catalogo.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fullcycle.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import com.fullcycle.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.infrastructure.genre.GenreClient;
import com.fullcycle.catalogo.infrastructure.genre.models.GenreEvent;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
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
public class GenreListener {

    private static final Logger LOG = LoggerFactory.getLogger(GenreListener.class);
    private static final TypeReference<MessageValue<GenreEvent>> GENRE_MESSAGE_TYPE = new TypeReference<>() {
    };

    private final GenreClient genreClient;
    private final SaveGenreUseCase saveGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;

    public GenreListener(
            final GenreClient genreClient,
            final SaveGenreUseCase saveGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase
    ) {
        this.genreClient = Objects.requireNonNull(genreClient);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.genres.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.genres.topics}",
            groupId = "${kafka.consumers.genres.group-id}",
            id = "${kafka.consumers.genres.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.genres.auto-offset-reset}"
            }
    )
    @RetryableTopic(
            backoff = @Backoff(delay = 1000, multiplier = 2),
            attempts = "${kafka.consumers.genres.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
        final var messagePayload = Json.readValue(payload, GENRE_MESSAGE_TYPE).payload();
        final var op = messagePayload.operation();

        if (Operation.isDelete(op)) {
            this.deleteGenreUseCase.execute(new DeleteGenreUseCase.Input(messagePayload.before().id()));
        } else {
            this.genreClient.genreOfId(messagePayload.after().id())
                    .map(it -> new SaveGenreUseCase.Input(it.id(), it.name(), it.isActive(), it.categoriesId(), it.createdAt(), it.updatedAt(), it.deletedAt()))
                    .ifPresentOrElse(this.saveGenreUseCase::execute, () -> {
                        LOG.warn("Genre was not found {}", messagePayload.after().id());
                    });
        }
    }

    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.warn("Message received from Kafka at DLT [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
    }
}
