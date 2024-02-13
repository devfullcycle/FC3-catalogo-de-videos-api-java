package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static com.fullcycle.catalogo.domain.utils.InstantUtils.now;

public class GenreElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private GenreElasticsearchGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(this.genreRepository);
        Assertions.assertNotNull(this.genreGateway);
    }

    @Test
    public void givenActiveGenreWithCategories_whenCallsSave_shouldPersistIt() {
        // given
        final var tech = Genre.with(IdUtils.uniqueId(), "Technology", true, Set.of("c1", "c2"), now(), now(), null);

        // when
        final var actualOutput = this.genreGateway.save(tech);

        // then
        Assertions.assertEquals(tech, actualOutput);

        final var actualGenre = this.genreRepository.findById(tech.id()).get();
        Assertions.assertEquals(tech.id(), actualGenre.id());
        Assertions.assertEquals(tech.name(), actualGenre.name());
        Assertions.assertEquals(tech.active(), actualGenre.active());
        Assertions.assertEquals(tech.categories(), actualGenre.categories());
        Assertions.assertEquals(tech.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(tech.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(tech.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenInactiveGenreWithoutCategories_whenCallsSave_shouldPersistIt() {
        // given
        final var tech = Genre.with(IdUtils.uniqueId(), "Technology", false, new HashSet<>(), now(), now(), now());

        // when
        final var actualOutput = this.genreGateway.save(tech);

        // then
        Assertions.assertEquals(tech, actualOutput);

        final var actualGenre = this.genreRepository.findById(tech.id()).get();
        Assertions.assertEquals(tech.id(), actualGenre.id());
        Assertions.assertEquals(tech.name(), actualGenre.name());
        Assertions.assertEquals(tech.active(), actualGenre.active());
        Assertions.assertEquals(tech.categories(), actualGenre.categories());
        Assertions.assertEquals(tech.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(tech.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(tech.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var tech = Fixture.Genres.tech();

        this.genreRepository.save(GenreDocument.from(tech));

        final var expectedId = tech.id();
        Assertions.assertTrue(this.genreRepository.existsById(expectedId));

        // when
        this.genreGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(this.genreRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "any";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.genreGateway.deleteById(expectedId));
    }
}