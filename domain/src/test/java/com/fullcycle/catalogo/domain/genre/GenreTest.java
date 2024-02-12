package com.fullcycle.catalogo.domain.genre;

import com.fullcycle.catalogo.domain.UnitTest;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import com.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class GenreTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateAGenre() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        // when
        final var actualGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // then
        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(expectedID, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertEquals(expectedDates, actualGenre.deletedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithGenre_thenInstantiateAGenre() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        final var aGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // then
        final var actualGenre = Genre.with(aGenre);

        // when
        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(aGenre.id(), actualGenre.id());
        Assertions.assertEquals(aGenre.name(), actualGenre.name());
        Assertions.assertEquals(aGenre.categories(), actualGenre.categories());
        Assertions.assertEquals(aGenre.active(), actualGenre.active());
        Assertions.assertEquals(aGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenNullCategories_whenCallWith_thenInstantiateAGenreWithEmptyCategories() {
        // given
        final List<String> expectedCategories = null;
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        // then
        final var actualGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        // when
        Assertions.assertNotNull(actualGenre);
        Assertions.assertEquals(aGenre.id(), actualGenre.id());
        Assertions.assertEquals(aGenre.name(), actualGenre.name());
        Assertions.assertNotNull(actualGenre.categories());
        Assertions.assertTrue(actualGenre.categories().isEmpty());
        Assertions.assertEquals(aGenre.active(), actualGenre.active());
        Assertions.assertEquals(aGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var expectedID = UUID.randomUUID().toString();
        final var expectedIsActive = true;
        final var expectedCategories = List.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        // when
        final var actualGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualGenre.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final var expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var expectedID = UUID.randomUUID().toString();
        final var expectedIsActive = true;
        final var expectedCategories = List.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        // when
        final var actualGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualGenre.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final String expectedID = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        // when
        final var actualGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualGenre.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final var expectedID = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = List.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        // when
        final var actualGenre =
                Genre.with(expectedID, expectedName, expectedCategories, expectedIsActive, expectedDates, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualGenre.validate(new ThrowsValidationHandler()));

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}