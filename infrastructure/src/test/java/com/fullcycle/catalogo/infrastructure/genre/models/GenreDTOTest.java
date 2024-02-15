package com.fullcycle.catalogo.infrastructure.genre.models;

import com.fullcycle.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

@JacksonTest
class GenreDTOTest {

    @Autowired
    private JacksonTester<GenreDTO> json;

    @Test
    public void testUnmarshall_shouldReadSnakeCaseResponse() throws IOException {
        final var genreResponse = """
                {
                    "id": "e7bf067b62a94cd1a8d1ccc56fb32bfb",
                    "name": "Tecnologia",
                    "categories_id": [
                        "dd940e76b7b948e8b663edc643c27601",
                        "a26ce442a369459f9a1579abe6727efc"
                    ],
                    "is_active": false,
                    "created_at": "2024-02-14T01:29:34.640227Z",
                    "updated_at": "2024-02-14T01:32:59.876638Z",
                    "deleted_at": "2024-02-14T01:32:59.876632Z"
                }
                """;

        final var actualGenre = this.json.parse(genreResponse);

        Assertions.assertThat(actualGenre)
                .hasFieldOrPropertyWithValue("id", "e7bf067b62a94cd1a8d1ccc56fb32bfb")
                .hasFieldOrPropertyWithValue("name", "Tecnologia")
                .hasFieldOrPropertyWithValue("categoriesId", Set.of("dd940e76b7b948e8b663edc643c27601", "a26ce442a369459f9a1579abe6727efc"))
                .hasFieldOrPropertyWithValue("isActive", false)
                .hasFieldOrPropertyWithValue("createdAt", Instant.parse("2024-02-14T01:29:34.640227Z"))
                .hasFieldOrPropertyWithValue("updatedAt", Instant.parse("2024-02-14T01:32:59.876638Z"))
                .hasFieldOrPropertyWithValue("deletedAt", Instant.parse("2024-02-14T01:32:59.876632Z"));
    }
}