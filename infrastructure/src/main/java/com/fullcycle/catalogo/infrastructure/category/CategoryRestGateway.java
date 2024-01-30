package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.infrastructure.authentication.GetClientCredentials;
import com.fullcycle.catalogo.infrastructure.category.models.CategoryDTO;
import com.fullcycle.catalogo.infrastructure.configuration.annotations.Categories;
import com.fullcycle.catalogo.infrastructure.utils.HttpClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-categories")
public class CategoryRestGateway implements CategoryGateway, HttpClient {

    public static final String NAMESPACE = "categories";

    private final RestClient restClient;
    private final GetClientCredentials getClientCredentials;

    public CategoryRestGateway(@Categories final RestClient categoryHttpClient, final GetClientCredentials getClientCredentials) {
        this.restClient = Objects.requireNonNull(categoryHttpClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Cacheable(key = "#categoryId")
    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<Category> categoryOfId(String categoryId) {
        final var token = this.getClientCredentials.retrieve();
        return doGet(categoryId, () ->
                this.restClient.get()
                        .uri("/{id}", categoryId)
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(categoryId))
                        .onStatus(is5xx, a5xxHandler(categoryId))
                        .body(CategoryDTO.class)
        )
                .map(CategoryDTO::toCategory);
    }
}
