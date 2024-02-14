package com.fullcycle.catalogo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.catalogo.infrastructure.category.CategoryRestGateway;
import com.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import com.fullcycle.catalogo.infrastructure.genre.GenreRestGateway;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration(exclude = {
        ElasticsearchRepositoriesAutoConfiguration.class,
        KafkaAutoConfiguration.class,
})
@SpringBootTest(classes = {WebServerConfig.class, IntegrationTestConfiguration.class})
@Tag("integrationTest")
public abstract class AbstractRestClientTest {

    protected static final String CATEGORY = CategoryRestGateway.NAMESPACE;
    protected static final String GENRE = GenreRestGateway.NAMESPACE;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void beforeEach() {
        WireMock.reset();
        WireMock.resetAllRequests();
        resetAllCaches();
        List.of(CATEGORY, GENRE).forEach(this::resetFaultTolerance);
    }

    protected Cache cache(final String name) {
        return cacheManager.getCache(name);
    }

    protected void checkCircuitBreakerState(final String name, final CircuitBreaker.State expectedState) {
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        Assertions.assertEquals(expectedState, cb.getState());
    }

    protected void acquireBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).acquirePermission();
    }

    protected void releaseBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).releasePermission();
    }

    protected void transitionToOpenState(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).transitionToOpenState();
    }

    protected void transitionToClosedState(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).transitionToClosedState();
    }

    protected String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetAllCaches() {
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    private void resetFaultTolerance(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).reset();
    }
}
