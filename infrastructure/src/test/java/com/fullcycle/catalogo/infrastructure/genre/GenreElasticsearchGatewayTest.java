package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
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

    @Test
    public void givenEmptyGenre_whenCallsFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedCategories = Set.<String>of();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = this.genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());
    }

    @ParameterizedTest
    @CsvSource({
            "mar,0,10,1,1,Marketing",
            "te,0,10,1,1,Technology"
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockGenres();

        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedCategories = Set.<String>of();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = this.genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "c123,0,10,1,1,Marketing",
            "c456,0,10,1,1,Technology",
            ",0,10,3,3,Business",
    })
    public void givenValidCategory_whenCallsFindAll_shouldReturnElementsFiltered(
            final String categories,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockGenres();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedCategories = categories == null ? Set.<String>of() : Set.of(categories);

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = this.genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Business",
            "name,desc,0,10,3,3,Technology",
            "created_at,asc,0,10,3,3,Technology",
            "created_at,desc,0,10,3,3,Marketing",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnElementsSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockGenres();

        final var expectedTerms = "";
        final var expectedCategories = Set.<String>of();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = this.genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Business",
            "1,1,1,3,Marketing",
            "2,1,1,3,Technology",
            "3,1,0,3,",
    })
    public void givenValidPage_whenCallsFindAll_shouldReturnElementsPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockGenres();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedCategories = Set.<String>of();

        final var aQuery =
                new GenreSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection, expectedCategories);

        // when
        final var actualOutput = this.genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());

        if (StringUtils.isNotEmpty(expectedName)) {
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    @Test
    public void givenValidIds_whenCallsFindAllById_shouldReturnElements() {
        // given
        final var tech = this.genreRepository.save(GenreDocument.from(Fixture.Genres.tech()));
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.business()));
        final var marketing = this.genreRepository.save(GenreDocument.from(Fixture.Genres.marketing()));

        final var expectedSize = 2;
        final var expectedIds = Set.of(tech.id(), marketing.id());

        // when
        final var actualOutput = this.genreGateway.findAllById(expectedIds);

        // then
        Assertions.assertEquals(expectedSize, actualOutput.size());

        final var actualIds = actualOutput.stream().map(Genre::id).toList();
        Assertions.assertTrue(expectedIds.containsAll(actualIds));
    }

    @Test
    public void givenNullIds_whenCallsFindAllById_shouldReturnEmpty() {
        // given
        final var expectedItems = List.of();
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = this.genreGateway.findAllById(expectedIds);

        // then
        Assertions.assertEquals(expectedItems, actualOutput);
    }

    @Test
    public void givenEmptyIds_whenCallsFindAllById_shouldReturnEmpty() {
        // given
        final var expectedItems = List.of();
        final Set<String> expectedIds = Set.of();

        // when
        final var actualOutput = this.genreGateway.findAllById(expectedIds);

        // then
        Assertions.assertEquals(expectedItems, actualOutput);
    }

    private void mockGenres() {
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.tech()));
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.business()));
        this.genreRepository.save(GenreDocument.from(Fixture.Genres.marketing()));
    }
}