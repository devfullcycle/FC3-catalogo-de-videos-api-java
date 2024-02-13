package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberDocument;
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

    @Test
    public void givenActiveGenreWithCategories_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var business = Genre.with(IdUtils.uniqueId(), "Business", true, Set.of("c1", "c2"), now(), now(), null);

        this.genreRepository.save(GenreDocument.from(business));

        final var expectedId = business.id();
        Assertions.assertTrue(this.genreRepository.existsById(expectedId));

        // when
        final var actualOutput = this.genreGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(business.id(), actualOutput.id());
        Assertions.assertEquals(business.name(), actualOutput.name());
        Assertions.assertEquals(business.active(), actualOutput.active());
        Assertions.assertEquals(business.categories(), actualOutput.categories());
        Assertions.assertEquals(business.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenInactiveGenreWithoutCategories_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var business = Genre.with(IdUtils.uniqueId(), "Business", false, Set.of(), now(), now(), now());

        this.genreRepository.save(GenreDocument.from(business));

        final var expectedId = business.id();
        Assertions.assertTrue(this.genreRepository.existsById(expectedId));

        // when
        final var actualOutput = this.genreGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(business.id(), actualOutput.id());
        Assertions.assertEquals(business.name(), actualOutput.name());
        Assertions.assertEquals(business.active(), actualOutput.active());
        Assertions.assertEquals(business.categories(), actualOutput.categories());
        Assertions.assertEquals(business.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(business.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(business.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = "any";

        // when
        final var actualOutput = this.genreGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }
}