package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import com.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import com.fullcycle.catalogo.infrastructure.genre.models.GenreDTO;
import com.fullcycle.catalogo.infrastructure.genre.models.GenreInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class GenreGraphQLController {

    private final ListGenreUseCase listGenreUseCase;
    private final SaveGenreUseCase saveGenreUseCase;

    public GenreGraphQLController(final ListGenreUseCase listGenreUseCase, final SaveGenreUseCase saveGenreUseCase) {
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
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

    @MutationMapping
    public SaveGenreUseCase.Output saveGenre(@Argument(name = "input") final GenreInput arg) {
        final var input =
                new SaveGenreUseCase.Input(arg.id(), arg.name(), arg.active(), arg.categories(), arg.createdAt(), arg.updatedAt(), arg.deletedAt());

        return this.saveGenreUseCase.execute(input);
    }
}
