package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import com.fullcycle.catalogo.application.video.get.GetVideoUseCase;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class VideoGraphQLController {

    private final ListVideoUseCase listVideoUseCase;
    private final SaveVideoUseCase saveVideoUseCase;
    private final GetVideoUseCase getVideoUseCase;
    private final CategoryGateway categoryGateway;

    public VideoGraphQLController(
            final ListVideoUseCase listVideoUseCase,
            final SaveVideoUseCase saveVideoUseCase,
            final GetVideoUseCase getVideoUseCase,
            CategoryGateway categoryGateway) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
        this.getVideoUseCase = Objects.requireNonNull(getVideoUseCase);
        this.categoryGateway = categoryGateway;
    }

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_SUBSCRIBER"})
    public List<ListVideoUseCase.Output> videos(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument(name = "year_launched") final Integer yearLaunched,
            @Argument final String rating,
            @Argument final Set<String> categories,
            @Argument final Set<String> castMembers,
            @Argument final Set<String> genres
    ) {
        final var input = new ListVideoUseCase.Input(page, perPage, search, sort, direction, rating, yearLaunched, categories, castMembers, genres);
        return this.listVideoUseCase.execute(input).data();
    }

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_SUBSCRIBER"})
    public GetVideoUseCase.Output videoOfId(@Argument String videoId) {
        return getVideoUseCase.execute(new GetVideoUseCase.Input(videoId)).orElse(null);
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    @Secured({"ROLE_ADMIN", "ROLE_SUBSCRIBER"})
    public List<ListCategoryOutput> categories(GetVideoUseCase.Output video) {
        return video.categories()
                .stream()
                .flatMap(it -> this.categoryGateway.findById(it).stream())
                .map(ListCategoryOutput::from)
                .toList();
    }
}
