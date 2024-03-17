package com.fullcycle.catalogo.application.genre.get;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidIds_whenCallsGetAllById_shouldReturnIt() {
        // given
        final var genres = List.of(
                Fixture.Genres.business(),
                Fixture.Genres.tech()
        );

        final var expectedItems = genres.stream()
                .map(GetAllByIdUseCase.Output::new)
                .toList();

        final var expectedIds = genres.stream().map(Genre::id).toList();

        when(this.genreGateway.findAllById(any()))
                .thenReturn(genres);

        // when
        final var actualOutput = this.useCase.execute(new GetAllByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.size() &&
                        expectedItems.containsAll(actualOutput)
        );

        verify(this.genreGateway, times(1)).findAllById(expectedIds);
    }

    @Test
    public void givenNullIds_whenCallsGetAllById_shouldReturnEmpty() {
        // given
        final List<String> expectedIds = null;

        // when
        final var actualOutput = this.useCase.execute(new GetAllByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(actualOutput.isEmpty());

        verify(this.genreGateway, times(0)).findAllById(any());
    }
}