package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.infrastructure.category.models.CategoryDTO;
import com.fullcycle.catalogo.infrastructure.utils.HttpClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class CategoryRestClient implements HttpClient {

    public static final String NAMESPACE = "categories";

    private final RestClient restClient;

    public CategoryRestClient(final RestClient categoryHttpClient) {
        this.restClient = categoryHttpClient;
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<CategoryDTO> getById(final String categoryId) {
        return doGet(categoryId, () ->
                this.restClient.get()
                        .uri("/{id}", categoryId)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(categoryId))
                        .onStatus(is5xx, a5xxHandler(categoryId))
                        .body(CategoryDTO.class)
        );
    }
}
