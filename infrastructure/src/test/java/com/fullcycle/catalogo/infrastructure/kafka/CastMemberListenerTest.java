package com.fullcycle.catalogo.infrastructure.kafka;

import com.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import com.fullcycle.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.infrastructure.castmember.models.CastMemberEvent;
import com.fullcycle.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.ValuePayload;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CastMemberListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @SpyBean
    private CastMemberListener castMemberListener;

    @Value("${kafka.consumers.cast-members.topics}")
    private String castmemberTopics;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testCastMemberTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.cast_members";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.cast_members-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.cast_members-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.cast_members-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.cast_members-dlt";

        // when
        final var actualTopics = admin().listTopics().listings().get(10, TimeUnit.SECONDS).stream()
                .map(TopicListing::name)
                .collect(Collectors.toSet());

        // then
        Assertions.assertTrue(actualTopics.contains(expectedMainTopic));
        Assertions.assertTrue(actualTopics.contains(expectedRetry0Topic));
        Assertions.assertTrue(actualTopics.contains(expectedRetry1Topic));
        Assertions.assertTrue(actualTopics.contains(expectedRetry2Topic));
        Assertions.assertTrue(actualTopics.contains(expectedDLTTopic));
    }

    @Test
    public void givenInvalidResponsesFromHandlerShouldRetryUntilGoesToDLT() throws Exception {
        // given
        final var expectedMaxAttempts = 4;
        final var expectedMaxDLTAttempts = 1;
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.cast_members";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.cast_members-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.cast_members-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.cast_members-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.cast_members-dlt";

        final var gabriel = Fixture.CastMembers.gabriel();
        final var gabrielEvent = CastMemberEvent.from(gabriel);

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(gabrielEvent, gabrielEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        doAnswer(t -> {
            latch.countDown();
            throw new RuntimeException("BOOM!");
        }).when(deleteCastMemberUseCase).execute(any());

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(castMemberListener).onDLTMessage(any(), any());

        // when
        producer().send(new ProducerRecord<>(castmemberTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(castMemberListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        verify(castMemberListener, times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());

        Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }
}