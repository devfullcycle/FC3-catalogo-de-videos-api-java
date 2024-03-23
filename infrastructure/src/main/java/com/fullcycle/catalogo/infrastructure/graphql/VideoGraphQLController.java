package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.fullcycle.catalogo.application.category.get.GetAllCategoriesByIdUseCase;
import com.fullcycle.catalogo.application.genre.get.GetAllGenresByIdUseCase;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import com.fullcycle.catalogo.infrastructure.castmember.GqlCastMemberPresenter;
import com.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;
import com.fullcycle.catalogo.infrastructure.category.GqlCategoryPresenter;
import com.fullcycle.catalogo.infrastructure.category.models.GqlCategory;
import com.fullcycle.catalogo.infrastructure.genre.GqlGenrePresenter;
import com.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;
import com.fullcycle.catalogo.infrastructure.video.GqlVideoPresenter;
import com.fullcycle.catalogo.infrastructure.video.models.GqlVideo;
import com.fullcycle.catalogo.infrastructure.video.models.GqlVideoInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
    private final SaveVideoUseCase saveVideoUseCase;

    public VideoGraphQLController(
            final ListVideoUseCase listVideoUseCase,
            final GetAllCastMembersByIdUseCase getAllCastMembersByIdUseCase,
            final GetAllCategoriesByIdUseCase getAllCategoriesByIdUseCase,
            final GetAllGenresByIdUseCase getAllGenresByIdUseCase,
            final SaveVideoUseCase saveVideoUseCase
    ) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.getAllCastMembersByIdUseCase = Objects.requireNonNull(getAllCastMembersByIdUseCase);
        this.getAllCategoriesByIdUseCase = Objects.requireNonNull(getAllCategoriesByIdUseCase);
        this.getAllGenresByIdUseCase = Objects.requireNonNull(getAllGenresByIdUseCase);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
    }

    @QueryMapping
    public List<GqlVideo> videos(
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
        return this.listVideoUseCase.execute(input)
                .map(GqlVideoPresenter::present)
                .data();
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    public List<GqlCastMember> castMembers(final GqlVideo video) {
        return this.getAllCastMembersByIdUseCase.execute(new GetAllCastMembersByIdUseCase.Input(video.castMembersId())).stream()
                .map(GqlCastMemberPresenter::present)
                .toList();
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    public List<GqlCategory> categories(final GqlVideo video) {
        return this.getAllCategoriesByIdUseCase.execute(new GetAllCategoriesByIdUseCase.Input(video.categoriesId())).stream()
                .map(GqlCategoryPresenter::present)
                .toList();
    }

    @SchemaMapping(typeName = "Video", field = "genres")
    public List<GqlGenre> genres(final GqlVideo video) {
        return this.getAllGenresByIdUseCase.execute(new GetAllGenresByIdUseCase.Input(video.genresId())).stream()
                .map(GqlGenrePresenter::present)
                .toList();
    }

    @MutationMapping
    public SaveVideoUseCase.Output saveVideo(@Argument(name = "input") final GqlVideoInput arg) {
        final var input = new SaveVideoUseCase.Input(
                arg.id(), arg.title(), arg.description(), arg.yearLaunched(), arg.duration(),
                arg.rating(), arg.opened(), arg.published(), arg.createdAt(), arg.updatedAt(),
                arg.video(), arg.trailer(), arg.banner(), arg.thumbnail(), arg.thumbnailHalf(),
                arg.categoriesId(), arg.castMembersId(), arg.genresId()
        );

        return this.saveVideoUseCase.execute(input);
    }
}
