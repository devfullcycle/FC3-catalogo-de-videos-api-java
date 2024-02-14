package com.fullcycle.catalogo.infrastructure.kafka;

import com.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import com.fullcycle.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.infrastructure.category.CategoryGateway;
import com.fullcycle.catalogo.infrastructure.category.models.CategoryEvent;
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

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryListenerTest extends AbstractEmbeddedKafkaTest {

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private SaveCategoryUseCase saveCategoryUseCase;

    @MockBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private CategoryListener categoryListener;

    @Value("${kafka.consumers.categories.topics}")
    private String categoryTopic;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testCategoriesTopics() throws Exception {
        // given
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.categories";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.categories-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.categories-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.categories-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.categories-dlt";

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
        final var expectedMainTopic = "adm_videos_mysql.adm_videos.categories";
        final var expectedRetry0Topic = "adm_videos_mysql.adm_videos.categories-retry-0";
        final var expectedRetry1Topic = "adm_videos_mysql.adm_videos.categories-retry-1";
        final var expectedRetry2Topic = "adm_videos_mysql.adm_videos.categories-retry-2";
        final var expectedDLTTopic = "adm_videos_mysql.adm_videos.categories-dlt";

        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(5);

        doAnswer(t -> {
            latch.countDown();
            if (latch.getCount() > 0) {
                throw new RuntimeException("BOOM!");
            }
            return null;
        }).when(deleteCategoryUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryListener, times(expectedMaxAttempts)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals(expectedMainTopic, allMetas.get(0).topic());
        Assertions.assertEquals(expectedRetry0Topic, allMetas.get(1).topic());
        Assertions.assertEquals(expectedRetry1Topic, allMetas.get(2).topic());
        Assertions.assertEquals(expectedRetry2Topic, allMetas.get(3).topic());

        verify(categoryListener, times(expectedMaxDLTAttempts)).onDLTMessage(eq(message), metadata.capture());

        Assertions.assertEquals(expectedDLTTopic, metadata.getValue().topic());
    }

    @Test
    public void givenUpdateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, aulasEvent, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return aulas;
        }).when(saveCategoryUseCase).execute(any());

        doReturn(Optional.of(aulas)).when(categoryGateway).categoryOfId(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryGateway, times(1)).categoryOfId(eq(aulas.id()));

        verify(saveCategoryUseCase, times(1)).execute(eq(aulas));
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(aulasEvent, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return aulas;
        }).when(saveCategoryUseCase).execute(any());

        doReturn(Optional.of(aulas)).when(categoryGateway).categoryOfId(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryGateway, times(1)).categoryOfId(eq(aulas.id()));

        verify(saveCategoryUseCase, times(1)).execute(eq(aulas));
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var aulasEvent = new CategoryEvent(aulas.id());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, aulasEvent, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(deleteCategoryUseCase).execute(any());

        // when
        producer().send(new ProducerRecord<>(categoryTopic, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(deleteCategoryUseCase, times(1)).execute(eq(aulas.id()));
    }
}