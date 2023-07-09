package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryElasticsearchGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(this.categoryGateway);
        Assertions.assertNotNull(this.categoryRepository);
    }

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var aulas = Fixture.Categories.aulas();

        // when
        final var actualOutput = this.categoryGateway.save(aulas);

        // then
        Assertions.assertEquals(aulas, actualOutput);

        final var actualCategory = this.categoryRepository.findById(aulas.id()).get();
        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.active(), actualCategory.active());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aulas = Fixture.Categories.aulas();

        this.categoryRepository.save(CategoryDocument.from(aulas));

        final var expectedId = aulas.id();
        Assertions.assertTrue(this.categoryRepository.existsById(expectedId));

        // when
        this.categoryGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(this.categoryRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "any";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.categoryGateway.deleteById(expectedId));
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var talks = Fixture.Categories.talks();

        this.categoryRepository.save(CategoryDocument.from(talks));

        final var expectedId = talks.id();
        Assertions.assertTrue(this.categoryRepository.existsById(expectedId));

        // when
        final var actualOutput = this.categoryGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(talks.id(), actualOutput.id());
        Assertions.assertEquals(talks.name(), actualOutput.name());
        Assertions.assertEquals(talks.description(), actualOutput.description());
        Assertions.assertEquals(talks.active(), actualOutput.active());
        Assertions.assertEquals(talks.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(talks.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(talks.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = "any";

        // when
        final var actualOutput = this.categoryGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }

    @Test
    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());
    }

    @ParameterizedTest
    @CsvSource({
            "aul,0,10,1,1,Aulas",
            "liv,0,10,1,1,Lives"
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
        mockCategories();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Aulas",
            "name,desc,0,10,3,3,Talks",
            "created_at,asc,0,10,3,3,Aulas",
            "created_at,desc,0,10,3,3,Lives",
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
        mockCategories();

        final var expectedTerms = "";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Aulas",
            "1,1,1,3,Lives",
            "2,1,1,3,Talks",
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
        mockCategories();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.categoryGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());

        if (StringUtils.isNotEmpty(expectedName)) {
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    private void mockCategories() {
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.aulas()));
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.talks()));
        this.categoryRepository.save(CategoryDocument.from(Fixture.Categories.lives()));
    }
}
