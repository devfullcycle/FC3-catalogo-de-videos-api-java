package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import com.fullcycle.catalogo.application.category.list.ListCategoryUseCase;
import com.fullcycle.catalogo.application.category.save.SaveCategoryUseCase;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.infrastructure.category.models.CategoryInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CategoryGraphQLController {

    private final ListCategoryUseCase listCategoryUseCase;
    private final SaveCategoryUseCase saveCategoryUseCase;

    public CategoryGraphQLController(
            final ListCategoryUseCase listCategoryUseCase,
            final SaveCategoryUseCase saveCategoryUseCase
    ) {
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
    }

    @QueryMapping
    @Secured({"ROLE_ADMIN", "ROLE_SUBSCRIBER"})
    public List<ListCategoryOutput> categories(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ) {

        final var aQuery =
                new CategorySearchQuery(page, perPage, search, sort, direction);

        return this.listCategoryUseCase.execute(aQuery).data();
    }

    @MutationMapping
    @Secured({"ROLE_ADMIN", "ROLE_CATALOGO_CATEGORIA"})
    public Category saveCategory(@Argument final CategoryInput input) {
        return this.saveCategoryUseCase.execute(input.toCategory());
    }
}
