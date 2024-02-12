package com.fullcycle.catalogo.application.genre.delete;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var business = Fixture.Genres.business();
        final var expectedId = business.id();

        doNothing()
                .when(this.genreGateway).deleteById(anyString());

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(this.genreGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(this.genreGateway, never()).deleteById(eq(expectedId));
    }
}
