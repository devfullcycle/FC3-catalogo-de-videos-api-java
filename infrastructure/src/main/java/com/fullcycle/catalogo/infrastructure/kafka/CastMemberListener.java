package com.fullcycle.catalogo.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fullcycle.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import com.fullcycle.catalogo.infrastructure.castmember.models.CastMemberEvent;
import com.fullcycle.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CastMemberListener {

    private static final Logger LOG = LoggerFactory.getLogger(CastMemberListener.class);

    public static final TypeReference<MessageValue<CastMemberEvent>> CAST_MEMBER_MESSAGE = new TypeReference<>() {
    };

    private final SaveCastMemberUseCase saveCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;

    public CastMemberListener(
            final SaveCastMemberUseCase saveCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase
    ) {
        this.saveCastMemberUseCase = Objects.requireNonNull(saveCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.cast-members.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.cast-members.topics}",
            groupId = "${kafka.consumers.cast-members.group-id}",
            id = "${kafka.consumers.cast-members.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.cast-members.auto-offset-reset}"
            }
    )
    @RetryableTopic(
            attempts = "${kafka.consumers.cast-members.max-attempts}",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE
    )
    public void onMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
        final var messagePayload = Json.readValue(payload, CAST_MEMBER_MESSAGE).payload();
        final var op = messagePayload.operation();

        if (op.isDelete()) {
            this.deleteCastMemberUseCase.execute(messagePayload.before().id());
        } else {
            this.saveCastMemberUseCase.execute(messagePayload.after().toCastMember());
        }
    }

    @DltHandler
    public void onDLTMessage(@Payload final String payload, final ConsumerRecordMetadata metadata) {
        LOG.warn("Message received from Kafka at DLT [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
    }
}
