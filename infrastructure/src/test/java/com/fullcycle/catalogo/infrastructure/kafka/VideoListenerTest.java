package com.fullcycle.catalogo.infrastructure.kafka;

import com.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import com.fullcycle.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.MessageValue;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.Operation;
import com.fullcycle.catalogo.infrastructure.kafka.models.connect.ValuePayload;
import com.fullcycle.catalogo.infrastructure.video.VideoClient;
import com.fullcycle.catalogo.infrastructure.video.models.ImageResourceDTO;
import com.fullcycle.catalogo.infrastructure.video.models.VideoDTO;
import com.fullcycle.catalogo.infrastructure.video.models.VideoEvent;
import com.fullcycle.catalogo.infrastructure.video.models.VideoResourceDTO;
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

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class VideoListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;

    @MockBean
    private VideoClient videoClient;

    @SpyBean
    private VideoListener videoListener;

    @Value("${kafka.consumers.videos.topics}")
    private String videoTopics;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testVideoTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.videos";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.videos-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.videos-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.videos-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.videos-dlt";

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
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.videos";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.videos-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.videos-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.videos-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.videos-dlt";

        final var systemDesign = Fixture.Videos.systemDesign();
        final var systemDesignEv = new VideoEvent(systemDesign.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(systemDesignEv, systemDesignEv, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        doAnswer(t -> {
            latch.countDown();
            throw new RuntimeException("BOOM!");
        }).when(deleteVideoUseCase).execute(any());

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(videoListener).onDLTMessage(any(), any());

        // when
        producer().send(new ProducerRecord<>(videoTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(videoListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        verify(videoListener, times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());

        Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }

    @Test
    public void givenValidVideoWhenUpdateOperationProcessGoesOKThenShouldEndTheOperation() throws Exception {
        // given
        final var golang = Fixture.Videos.golang();
        final var golangEv = new VideoEvent(golang.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(golangEv, golangEv, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return new SaveVideoUseCase.Output(golang.id());
        }).when(saveVideoUseCase).execute(any());

        doReturn(Optional.of(videoDto(golang))).when(videoClient).videoOfId(any());

        // when
        producer().send(new ProducerRecord<>(videoTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(videoClient, times(1)).videoOfId(eq(golang.id()));

        verify(saveVideoUseCase, times(1)).execute(refEq(new SaveVideoUseCase.Input(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.launchedAt().getValue(),
                golang.duration(),
                golang.rating().getName(),
                golang.opened(),
                golang.published(),
                golang.createdAt().toString(),
                golang.updatedAt().toString(),
                golang.video(),
                golang.trailer(),
                golang.banner(),
                golang.thumbnail(),
                golang.thumbnailHalf(),
                golang.categories(),
                golang.castMembers(),
                golang.genres()
        )));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var golang = Fixture.Videos.golang();
        final var golangEv = new VideoEvent(golang.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(golangEv, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return new SaveVideoUseCase.Output(golang.id());
        }).when(saveVideoUseCase).execute(any());

        doReturn(Optional.of(videoDto(golang))).when(videoClient).videoOfId(any());

        // when
        producer().send(new ProducerRecord<>(videoTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(videoClient, times(1)).videoOfId(eq(golang.id()));

        verify(saveVideoUseCase, times(1)).execute(refEq(new SaveVideoUseCase.Input(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.launchedAt().getValue(),
                golang.duration(),
                golang.rating().getName(),
                golang.opened(),
                golang.published(),
                golang.createdAt().toString(),
                golang.updatedAt().toString(),
                golang.video(),
                golang.trailer(),
                golang.banner(),
                golang.thumbnail(),
                golang.thumbnailHalf(),
                golang.categories(),
                golang.castMembers(),
                golang.genres()
        )));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var golang = Fixture.Videos.golang();
        final var golangEv = new VideoEvent(golang.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, golangEv, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(deleteVideoUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(videoTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(deleteVideoUseCase, times(1)).execute(eq(new DeleteVideoUseCase.Input(golang.id())));
    }

    private static VideoDTO videoDto(final Video aVideo) {
        return new VideoDTO(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                aVideo.launchedAt().getValue(),
                aVideo.rating().getName(),
                aVideo.duration(),
                aVideo.opened(),
                aVideo.published(),
                videoResourceDTO(aVideo.video()),
                videoResourceDTO(aVideo.trailer()),
                imageResourceDTO(aVideo.banner()),
                imageResourceDTO(aVideo.thumbnail()),
                imageResourceDTO(aVideo.thumbnailHalf()),
                aVideo.categories(),
                aVideo.castMembers(),
                aVideo.genres(),
                aVideo.createdAt().toString(),
                aVideo.updatedAt().toString()
        );
    }

    private static VideoResourceDTO videoResourceDTO(final String data) {
        return new VideoResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data, data, "processed");
    }

    private static ImageResourceDTO imageResourceDTO(final String data) {
        return new ImageResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data);
    }
}