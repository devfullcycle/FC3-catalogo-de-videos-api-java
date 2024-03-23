package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.AbstractRestClientTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.infrastructure.authentication.ClientCredentialsManager;
import com.fullcycle.catalogo.infrastructure.video.models.ImageResourceDTO;
import com.fullcycle.catalogo.infrastructure.video.models.VideoDTO;
import com.fullcycle.catalogo.infrastructure.video.models.VideoResourceDTO;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.doReturn;

class VideoRestClientTest extends AbstractRestClientTest {

    @Autowired
    private VideoRestClient target;

    @SpyBean
    private ClientCredentialsManager credentialsManager;

    // OK
    @Test
    public void givenAVideo_whenReceive200FromServer_shouldBeOk() {
        // given
        final var golang = Fixture.Videos.golang();

        final var responseBody = writeValueAsString(new VideoDTO(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.launchedAt().getValue(),
                golang.rating().getName(),
                golang.duration(),
                golang.opened(),
                golang.published(),
                videoResourceDTO(golang.video()),
                videoResourceDTO(golang.trailer()),
                imageResourceDTO(golang.banner()),
                imageResourceDTO(golang.thumbnail()),
                imageResourceDTO(golang.thumbnailHalf()),
                golang.categories(),
                golang.castMembers(),
                golang.genres(),
                golang.createdAt().toString(),
                golang.updatedAt().toString()
        ));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/videos/%s".formatted(golang.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualVideo = target.videoOfId(golang.id()).get();

        // then
        Assertions.assertEquals(golang.id(), actualVideo.id());
        Assertions.assertEquals(golang.createdAt().toString(), actualVideo.createdAt());
        Assertions.assertEquals(golang.updatedAt().toString(), actualVideo.updatedAt());
        Assertions.assertEquals(golang.title(), actualVideo.title());
        Assertions.assertEquals(golang.description(), actualVideo.description());
        Assertions.assertEquals(golang.launchedAt().getValue(), actualVideo.yearLaunched());
        Assertions.assertEquals(golang.duration(), actualVideo.duration());
        Assertions.assertEquals(golang.opened(), actualVideo.opened());
        Assertions.assertEquals(golang.published(), actualVideo.published());
        Assertions.assertEquals(golang.rating().getName(), actualVideo.rating());
        Assertions.assertEquals(golang.categories(), actualVideo.categoriesId());
        Assertions.assertEquals(golang.genres(), actualVideo.genresId());
        Assertions.assertEquals(golang.castMembers(), actualVideo.castMembersId());
        Assertions.assertEquals(golang.video(), actualVideo.video().encodedLocation());
        Assertions.assertEquals(golang.trailer(), actualVideo.trailer().encodedLocation());
        Assertions.assertEquals(golang.banner(), actualVideo.banner().location());
        Assertions.assertEquals(golang.thumbnail(), actualVideo.thumbnail().location());
        Assertions.assertEquals(golang.thumbnailHalf(), actualVideo.thumbnailHalf().location());

        verify(1, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(golang.id()))));
    }

    @Test
    public void givenAVideo_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var golang = Fixture.Videos.golang();

        final var responseBody = writeValueAsString(new VideoDTO(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.launchedAt().getValue(),
                golang.rating().getName(),
                golang.duration(),
                golang.opened(),
                golang.published(),
                videoResourceDTO(golang.video()),
                videoResourceDTO(golang.trailer()),
                imageResourceDTO(golang.banner()),
                imageResourceDTO(golang.thumbnail()),
                imageResourceDTO(golang.thumbnailHalf()),
                golang.categories(),
                golang.castMembers(),
                golang.genres(),
                golang.createdAt().toString(),
                golang.updatedAt().toString()
        ));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/videos/%s".formatted(golang.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        target.videoOfId(golang.id()).get();
        target.videoOfId(golang.id()).get();
        final var actualVideo = target.videoOfId(golang.id()).get();

        // then
        Assertions.assertEquals(golang.id(), actualVideo.id());
        Assertions.assertEquals(golang.createdAt().toString(), actualVideo.createdAt());
        Assertions.assertEquals(golang.updatedAt().toString(), actualVideo.updatedAt());
        Assertions.assertEquals(golang.title(), actualVideo.title());
        Assertions.assertEquals(golang.description(), actualVideo.description());
        Assertions.assertEquals(golang.launchedAt().getValue(), actualVideo.yearLaunched());
        Assertions.assertEquals(golang.duration(), actualVideo.duration());
        Assertions.assertEquals(golang.opened(), actualVideo.opened());
        Assertions.assertEquals(golang.published(), actualVideo.published());
        Assertions.assertEquals(golang.rating().getName(), actualVideo.rating());
        Assertions.assertEquals(golang.categories(), actualVideo.categoriesId());
        Assertions.assertEquals(golang.genres(), actualVideo.genresId());
        Assertions.assertEquals(golang.castMembers(), actualVideo.castMembersId());
        Assertions.assertEquals(golang.video(), actualVideo.video().encodedLocation());
        Assertions.assertEquals(golang.trailer(), actualVideo.trailer().encodedLocation());
        Assertions.assertEquals(golang.banner(), actualVideo.banner().location());
        Assertions.assertEquals(golang.thumbnail(), actualVideo.thumbnail().location());
        Assertions.assertEquals(golang.thumbnailHalf(), actualVideo.thumbnailHalf().location());

        final var actualCachedValue = cache("admin-videos").get(golang.id());
        Assertions.assertEquals(actualVideo, actualCachedValue.get());

        verify(1, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(golang.id()))));
    }

    // 5XX
    @Test
    public void givenAVideo_whenReceive5xxFromServer_shouldReturnInternalError() {
        // given
        final var expectedId = "456";
        final var expectedErrorMessage = "Error observed from videos [resourceId:%s] [status:500]".formatted(expectedId);
        final var expectedRetries = 2;

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualEx = Assertions.assertThrows(InternalErrorException.class, () -> target.videoOfId(expectedId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(expectedRetries, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    // 404
    @Test
    public void givenAVideo_whenReceive404NotFoundFromServer_shouldReturnEmpty() {
        // given
        final var expectedId = "123";
        final var expectedRetries = 1;

        final var responseBody = writeValueAsString(Map.of("message", "Not found"));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualGenre = target.videoOfId(expectedId);

        // then
        Assertions.assertTrue(actualGenre.isEmpty());

        verify(expectedRetries, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    // Timeout
    @Test
    public void givenAVideo_whenReceiveTimeout_shouldReturnInternalError() {
        // given
        final var golang = Fixture.Videos.golang();

        final var expectedRetries = 2;
        final var expectedErrorMessage = "Timeout observed from videos [resourceId:%s]".formatted(golang.id());

        final var responseBody = writeValueAsString(new VideoDTO(
                golang.id(),
                golang.title(),
                golang.description(),
                golang.launchedAt().getValue(),
                golang.rating().getName(),
                golang.duration(),
                golang.opened(),
                golang.published(),
                videoResourceDTO(golang.video()),
                videoResourceDTO(golang.trailer()),
                imageResourceDTO(golang.banner()),
                imageResourceDTO(golang.thumbnail()),
                imageResourceDTO(golang.thumbnailHalf()),
                golang.categories(),
                golang.castMembers(),
                golang.genres(),
                golang.createdAt().toString(),
                golang.updatedAt().toString()
        ));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/videos/%s".formatted(golang.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withFixedDelay(600)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualEx = Assertions.assertThrows(InternalErrorException.class, () -> target.videoOfId(golang.id()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(expectedRetries, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(golang.id()))));
    }

    @Test
    public void givenAVideo_whenBulkheadIsFull_shouldReturnError() {
        // given
        final var expectedErrorMessage = "Bulkhead 'videos' is full and does not permit further calls";
        final var expectedId = "123";

        acquireBulkheadPermission(VIDEO);

        // when
        final var actualEx = Assertions.assertThrows(BulkheadFullException.class, () -> target.videoOfId(expectedId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        releaseBulkheadPermission(VIDEO);

        verify(0, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    @Test
    public void givenCall_whenCBIsOpen_shouldReturnError() {
        // given
        transitionToOpenState(VIDEO);
        final var expectedId = "123";
        final var expectedErrorMessage = "CircuitBreaker 'videos' is OPEN and does not permit further calls";

        // when
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.videoOfId(expectedId));

        // then
        checkCircuitBreakerState(VIDEO, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(0, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreaker() {
        // given
        final var expectedId = "123";
        final var expectedErrorMessage = "CircuitBreaker 'videos' is OPEN and does not permit further calls";

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/videos/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        Assertions.assertThrows(InternalErrorException.class, () -> this.target.videoOfId(expectedId));
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.videoOfId(expectedId));

        // then
        checkCircuitBreakerState(VIDEO, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(3, getRequestedFor(urlPathEqualTo("/api/videos/%s".formatted(expectedId))));
    }

    private static VideoResourceDTO videoResourceDTO(final String data) {
        return new VideoResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data, data, "processed");
    }

    private static ImageResourceDTO imageResourceDTO(final String data) {
        return new ImageResourceDTO(IdUtils.uniqueId(), IdUtils.uniqueId(), data, data);
    }
}