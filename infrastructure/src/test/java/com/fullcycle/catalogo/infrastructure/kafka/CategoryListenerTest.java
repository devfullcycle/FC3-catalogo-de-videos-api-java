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
    public void givenInvalidResponsesFromHandlerShouldRetryUntilGoesToDLT() throws Exception {
        // given
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
        producer().send(new ProducerRecord<>(categoryTopic, message));
        producer().flush();

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
        verify(categoryListener, times(4)).onMessage(eq(message), metadata.capture());

        final var allMetas = metadata.getAllValues();
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories", allMetas.get(0).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-0", allMetas.get(1).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-1", allMetas.get(2).topic());
        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-retry-2", allMetas.get(3).topic());

        verify(categoryListener, times(1)).onDLTMessage(eq(message), metadata.capture());

        Assertions.assertEquals("adm_videos_mysql.adm_videos.categories-dlt", metadata.getValue().topic());
    }
}