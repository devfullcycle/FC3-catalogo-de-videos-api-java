package com.fullcycle.catalogo.application.genre.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllByIdUseCase extends UseCase<GetAllByIdUseCase.Input, List<GetAllByIdUseCase.Output>> {

    private final GenreGateway genreGateway;

    public GetAllByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public List<Output> execute(final Input in) {
        if (in.ids().isEmpty()) {
            return List.of();
        }

        return this.genreGateway.findAllById(in.ids()).stream()
                .map(Output::new)
                .toList();
    }

    public record Input(List<String> ids) {
        @Override
        public List<String> ids() {
            return ids != null ? ids : Collections.emptyList();
        }
    }

    public record Output(
            String id,
            String name,
            boolean active,
            Set<String> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {

        public Output(final Genre genre) {
            this(
                    genre.id(),
                    genre.name(),
                    genre.active(),
                    genre.categories(),
                    genre.createdAt(),
                    genre.updatedAt(),
                    genre.deletedAt()
            );
        }
    }
}
