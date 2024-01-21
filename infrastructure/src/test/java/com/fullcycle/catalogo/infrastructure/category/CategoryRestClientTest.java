package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.AbstractRestClientTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.infrastructure.category.models.CategoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class CategoryRestClientTest extends AbstractRestClientTest {


    @Autowired
    private CategoryRestClient target;

    // OK
    @Test
    public void givenACategory_whenReceive200FromServer_shouldBeOk() {
        // given
        final var aulas = Fixture.Categories.aulas();

        final var responseBody = writeValueAsString(new CategoryDTO(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));

        stubFor(
                get(urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualCategory = target.getById(aulas.id()).get();

        // then
        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.active(), actualCategory.active());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());
    }

    // 5XX
    @Test
    public void givenACategory_whenReceive5xxFromServer_shouldReturnInternalError() {
        // given
        final var expectedId = "123";
        final var expectedErrorMessage = "Error observed from categories [resourceId:%s] [status:500]".formatted(expectedId);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        stubFor(
                get(urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualEx = Assertions.assertThrows(InternalErrorException.class, () -> target.getById(expectedId));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());
    }

    // 404
    @Test
    public void givenACategory_whenReceive404NotFoundFromServer_shouldReturnEmpty() {
        // given
        final var expectedId = "123";
        final var responseBody = writeValueAsString(Map.of("message", "Not found"));

        stubFor(
                get(urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualCategory = target.getById(expectedId);

        // then
        Assertions.assertTrue(actualCategory.isEmpty());
    }

    // Timeout
    @Test
    public void givenACategory_whenReceiveTimeout_shouldReturnInternalError() {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var expectedErrorMessage = "Timeout observed from categories [resourceId:%s]".formatted(aulas.id());

        final var responseBody = writeValueAsString(new CategoryDTO(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));


        stubFor(
                get(urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withFixedDelay(600)
                                .withBody(responseBody)
                        )
        );

        // when
        final var actualEx = Assertions.assertThrows(InternalErrorException.class, () -> target.getById(aulas.id()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualEx.getMessage());
    }
}
