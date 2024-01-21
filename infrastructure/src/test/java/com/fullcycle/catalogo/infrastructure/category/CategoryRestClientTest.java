package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.IntegrationTestConfiguration;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.infrastructure.category.models.CategoryDTO;
import com.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration(exclude = {
        ElasticsearchRepositoriesAutoConfiguration.class,
        KafkaAutoConfiguration.class,
})
@SpringBootTest(classes = {WebServerConfig.class, IntegrationTestConfiguration.class})
@Tag("integrationTest")
public class CategoryRestClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRestClient target;

    // OK
    @Test
    public void givenACategory_whenReceive200FromServer_shouldBeOk() throws IOException {
        // given
        final var aulas = Fixture.Categories.aulas();

        final var responseBody = objectMapper.writeValueAsString(new CategoryDTO(
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
    public void givenACategory_whenReceive5xxFromServer_shouldReturnInternalError() throws IOException {
        // given
        final var expectedId = "123";
        final var expectedErrorMessage = "Failed to get Category of id %s".formatted(expectedId);

        final var responseBody = objectMapper.writeValueAsString(Map.of("message", "Internal Server Error"));

        stubFor(
                get(urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withStatus(500)
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
    public void givenACategory_whenReceive404NotFoundFromServer_shouldReturnEmpty() throws IOException {
        // given
        final var expectedId = "123";
        final var responseBody = objectMapper.writeValueAsString(Map.of("message", "Not found"));

        stubFor(
                get(urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(aResponse()
                                .withStatus(404)
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
    public void givenACategory_whenReceiveTimeout_shouldReturnInternalError() throws IOException {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var expectedErrorMessage = "Timeout from category of ID %s".formatted(aulas.id());

        final var responseBody = objectMapper.writeValueAsString(new CategoryDTO(
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
