package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.infrastructure.authentication.GetClientCredentials;
import com.fullcycle.catalogo.infrastructure.configuration.annotations.Videos;
import com.fullcycle.catalogo.infrastructure.utils.HttpClient;
import com.fullcycle.catalogo.infrastructure.video.models.VideoDTO;
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
@CacheConfig(cacheNames = "admin-videos")
public class VideoRestClient implements VideoClient, HttpClient {

    public static final String NAMESPACE = "videos";

    private final RestClient restClient;
    private final GetClientCredentials getClientCredentials;

    public VideoRestClient(@Videos final RestClient restClient, final GetClientCredentials getClientCredentials) {
        this.restClient = restClient;
        this.getClientCredentials = getClientCredentials;
    }

    @Override
    @Cacheable(key = "#videoId")
    @Bulkhead(name = NAMESPACE)
    @CircuitBreaker(name = NAMESPACE)
    @Retry(name = NAMESPACE)
    public Optional<VideoDTO> videoOfId(String videoId) {
        final var token = this.getClientCredentials.retrieve();
        return doGet(videoId, () ->
                this.restClient.get()
                        .uri("/{id}", videoId)
                        .header(HttpHeaders.AUTHORIZATION, "bearer " + token)
                        .retrieve()
                        .onStatus(isNotFound, notFoundHandler(videoId))
                        .onStatus(is5xx, a5xxHandler(videoId))
                        .body(VideoDTO.class)
        );
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }
}
