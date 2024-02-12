package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class GenreGraphQLController {

    private final ListGenreUseCase listGenreUseCase;

    public GenreGraphQLController(final ListGenreUseCase listGenreUseCase) {
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
    }

    @QueryMapping
    public List<ListGenreUseCase.Output> genres(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final Set<String> categories
    ) {
        final var input = new ListGenreUseCase.Input(page, perPage, search, sort, direction, categories);
        return this.listGenreUseCase.execute(input).data();
    }
}
