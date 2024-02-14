package com.fullcycle.catalogo.application.genre.list;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import com.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.Objects;
import java.util.Set;

public class ListGenreUseCase extends UseCase<ListGenreUseCase.Input, Pagination<ListGenreUseCase.Output>> {

    private final GenreGateway genreGateway;

    public ListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<Output> execute(final ListGenreUseCase.Input input) {
        final var aQuery = new GenreSearchQuery(
                input.page(),
                input.perPage(),
                input.terms(),
                input.sort(),
                input.direction(),
                input.categories()
        );

        return this.genreGateway.findAll(aQuery)
                .map(Output::from);
    }

    public record Input(
            int page,
            int perPage,
            String terms,
            String sort,
            String direction,
            Set<String> categories
    ) {

    }

    public record Output(String id, String name) {

        public static Output from(Genre genre) {
            return new Output(genre.id(), genre.name());
        }
    }
}
