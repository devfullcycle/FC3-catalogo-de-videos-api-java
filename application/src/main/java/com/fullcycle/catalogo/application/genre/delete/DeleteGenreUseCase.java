package com.fullcycle.catalogo.application.genre.delete;

import com.fullcycle.catalogo.application.UnitUseCase;
import com.fullcycle.catalogo.domain.genre.GenreGateway;

import java.util.Objects;

public class DeleteGenreUseCase extends UnitUseCase<DeleteGenreUseCase.Input> {

    private final GenreGateway genreGateway;

    public DeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final Input input) {
        if (input == null || input.genreId() == null) {
            return;
        }

        this.genreGateway.deleteById(input.genreId());
    }

    public record Input(String genreId) {
    }
}
