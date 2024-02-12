package com.fullcycle.catalogo.application.genre.save;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SaveGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        when(genreGateway.save(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var input =
                new SaveGenreUseCase.Input(expectedID, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates);

        final var actualOutput = this.useCase.execute(input);

        // then
        verify(genreGateway, times(1)).save(eq(aCategory));

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedID, actualOutput.id());
    }

    @Test
    public void givenNullInput_whenCallsSave_shouldReturnError() {
        // given
        final SaveGenreUseCase.Input input = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'SaveGenreUseCase.Input' cannot be null";

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(input));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(genreGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given
        final String expectedID = null;
        final var expectedName = "Business";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var input =
                new SaveGenreUseCase.Input(expectedID, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates);

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(genreGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidName_whenCallsSave_shouldReturnError() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final String expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedDates = InstantUtils.now();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var input =
                new SaveGenreUseCase.Input(expectedID, expectedName, expectedIsActive, expectedCategories, expectedDates, expectedDates, expectedDates);

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(genreGateway, times(0)).save(any());
    }
}
