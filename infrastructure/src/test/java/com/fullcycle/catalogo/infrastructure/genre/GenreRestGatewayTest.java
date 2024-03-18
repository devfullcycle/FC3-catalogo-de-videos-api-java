package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.AbstractRestClientTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.infrastructure.authentication.ClientCredentialsManager;
import com.fullcycle.catalogo.infrastructure.genre.models.GenreDTO;
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

class GenreRestGatewayTest extends AbstractRestClientTest {

    @Autowired
    private GenreRestClient target;

    @SpyBean
    private ClientCredentialsManager credentialsManager;

    // OK
    @Test
    public void givenAGenre_whenReceive200FromServer_shouldBeOk() {
        // given
        final var tech = Fixture.Genres.tech();

        final var responseBody = writeValueAsString(new GenreDTO(
                tech.id(),
                tech.name(),
                tech.active(),
                tech.categories(),
                tech.createdAt(),
                tech.updatedAt(),
                tech.deletedAt()
        ));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/genres/%s".formatted(tech.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualGenre = target.genreOfId(tech.id()).get();

        // then
        Assertions.assertEquals(tech.id(), actualGenre.id());
        Assertions.assertEquals(tech.name(), actualGenre.name());
        Assertions.assertEquals(tech.active(), actualGenre.isActive());
        Assertions.assertEquals(tech.categories(), actualGenre.categoriesId());
        Assertions.assertEquals(tech.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(tech.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(tech.deletedAt(), actualGenre.deletedAt());

        verify(1, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(tech.id()))));
    }

    @Test
    public void givenAGenre_whenReceiveTwoCalls_shouldReturnCachedValue() {
        // given
        final var business = Fixture.Genres.business();

        final var responseBody = writeValueAsString(new GenreDTO(
                business.id(),
                business.name(),
                business.active(),
                business.categories(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        target.genreOfId(business.id()).get();
        target.genreOfId(business.id()).get();
        final var actualGenre = target.genreOfId(business.id()).get();

        // then
        Assertions.assertEquals(business.id(), actualGenre.id());
        Assertions.assertEquals(business.name(), actualGenre.name());
        Assertions.assertEquals(business.active(), actualGenre.isActive());
        Assertions.assertEquals(business.categories(), actualGenre.categoriesId());
        Assertions.assertEquals(business.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualGenre.deletedAt());

        final var actualCachedValue = cache("admin-genres").get(business.id());
        Assertions.assertEquals(actualGenre, actualCachedValue.get());

        verify(1, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(business.id()))));
    }

    // 5XX
    @Test
    public void givenAGenre_whenReceive5xxFromServer_shouldReturnInternalError() {
        // given
        final var expectedId = "456";
        final var expectedErrorMessage = "Error observed from genres [resourceId:%s] [status:500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualEx = Assertions.assertThrows(InternalErrorException.class, () -> target.genreOfId(expectedId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(2, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }

    // 404
    @Test
    public void givenAGenre_whenReceive404NotFoundFromServer_shouldReturnEmpty() {
        // given
        final var expectedId = "123";
        final var responseBody = writeValueAsString(Map.of("message", "Not found"));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualGenre = target.genreOfId(expectedId);

        // then
        Assertions.assertTrue(actualGenre.isEmpty());

        verify(1, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }

    // Timeout
    @Test
    public void givenAGenre_whenReceiveTimeout_shouldReturnInternalError() {
        // given
        final var business = Fixture.Genres.business();
        final var expectedErrorMessage = "Timeout observed from genres [resourceId:%s]".formatted(business.id());

        final var responseBody = writeValueAsString(new GenreDTO(
                business.id(),
                business.name(),
                business.active(),
                business.categories(),
                business.createdAt(),
                business.updatedAt(),
                business.deletedAt()
        ));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/genres/%s".formatted(business.id())))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withFixedDelay(600)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualEx = Assertions.assertThrows(InternalErrorException.class, () -> target.genreOfId(business.id()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(2, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(business.id()))));
    }

    @Test
    public void givenAGenre_whenBulkheadIsFull_shouldReturnError() {
        // given
        final var expectedErrorMessage = "Bulkhead 'genres' is full and does not permit further calls";

        acquireBulkheadPermission(GENRE);

        // when
        final var actualEx = Assertions.assertThrows(BulkheadFullException.class, () -> target.genreOfId("123"));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        releaseBulkheadPermission(GENRE);
    }

    @Test
    public void givenCall_whenCBIsOpen_shouldReturnError() {
        // given
        transitionToOpenState(GENRE);
        final var expectedId = "123";
        final var expectedErrorMessage = "CircuitBreaker 'genres' is OPEN and does not permit further calls";

        // when
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.genreOfId(expectedId));

        // then
        checkCircuitBreakerState(GENRE, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(0, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }

    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreaker() {
        // given
        final var expectedId = "123";
        final var expectedErrorMessage = "CircuitBreaker 'genres' is OPEN and does not permit further calls";

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        final var expectedToken = "access-123";
        doReturn(expectedToken).when(credentialsManager).retrieve();

        stubFor(
                get(urlPathEqualTo("/api/genres/%s".formatted(expectedId)))
                        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("bearer %s".formatted(expectedToken)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        Assertions.assertThrows(InternalErrorException.class, () -> this.target.genreOfId(expectedId));
        final var actualEx = Assertions.assertThrows(CallNotPermittedException.class, () -> this.target.genreOfId(expectedId));

        // then
        checkCircuitBreakerState(GENRE, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());

        verify(3, getRequestedFor(urlPathEqualTo("/api/genres/%s".formatted(expectedId))));
    }
}