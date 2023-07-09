package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
