package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.fullcycle.catalogo.application.category.get.GetAllCategoriesByIdUseCase;
import com.fullcycle.catalogo.application.genre.get.GetAllGenresByIdUseCase;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class VideoGraphQLController {

    private final ListVideoUseCase listVideoUseCase;
    private final GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase;
    private final GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase;
    private final GetAllGenresByIdUseCase getAllGenresByIdUseCase;

    public VideoGraphQLController(
            final ListVideoUseCase listVideoUseCase,
            final GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase,
            final GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase,
            final GetAllGenresByIdUseCase getAllGenresByIdUseCase
    ) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.getAllCastMembersByIdUseCase = Objects.requireNonNull(getAllCastMembersByIdUseCase);
        this.getAllCategoriesByIdUseCase = Objects.requireNonNull(getAllCategoriesByIdUseCase);
        this.getAllGenresByIdUseCase = Objects.requireNonNull(getAllGenresByIdUseCase);
    }

    @QueryMapping
    public List<ListVideoUseCase.Output> videos(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final String rating,
            @Argument final Integer yearLaunched,
            @Argument final Set<String> castMembers,
            @Argument final Set<String> categories,
            @Argument final Set<String> genres
    ) {
        final var input = new ListVideoUseCase.Input(page, perPage, search, sort, direction, rating, yearLaunched, categories, castMembers, genres);
        return this.listVideoUseCase.execute(input).data();
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    public List<GetAllCastMembersByIdUseCase.Output> castMembers(ListVideoUseCase.Output video) {
        return this.getAllCastMembersByIdUseCase.execute(new GetAllCastMembersByIdUseCase.Input(video.castMembersId()));
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    public List<GetAllCategoriesByIdUseCase.Output> categories(ListVideoUseCase.Output video) {
        return this.getAllCategoriesByIdUseCase.execute(new GetAllCategoriesByIdUseCase.Input(video.categoriesId()));
    }

    @SchemaMapping(typeName = "Video", field = "genres")
    public List<GetAllGenresByIdUseCase.Output> genres(ListVideoUseCase.Output video) {
        return this.getAllGenresByIdUseCase.execute(new GetAllGenresByIdUseCase.Input(video.castMembersId()));
    }
}
