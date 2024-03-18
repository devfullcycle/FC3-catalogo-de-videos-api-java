package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.infrastructure.authentication.GetClientCredentials;
import com.fullcycle.catalogo.infrastructure.configuration.annotations.Genres;
import com.fullcycle.catalogo.infrastructure.genre.models.GenreDTO;
import com.fullcycle.catalogo.infrastructure.utils.HttpClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-genres")
public class GenreRestClient implements GenreClient, HttpClient {

    public static final String NAMESPACE = "genres";

    private final RestClient restClient;
    private final GetClientCredentials getClientCredentials;

    public GenreRestClient(@Genres final RestClient restClient, final GetClientCredentials getClientCredentials) {
        this.restClient = restClient;
        this.getClientCredentials = getClientCredentials;
    }

    @Override
    @Cacheable(key = "#genreId")
    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<GenreDTO> genreOfId(String genreId) {
        final var token = this.getClientCredentials.retrieve();
        return doGet(genreId, () ->
                this.restClient.get()
                        .uri("/{id}", genreId)
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(genreId))
                        .onStatus(is5xx, a5xxHandler(genreId))
                        .body(GenreDTO.class)
        );
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }
}
