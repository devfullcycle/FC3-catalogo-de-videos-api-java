package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.application.category.get.GetAllCategoriesByIdUseCase;
import com.fullcycle.catalogo.application.category.list.ListCategoryOutput;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.infrastructure.category.models.GqlCategory;

public final class GqlCategoryPresenter {

    private GqlCategoryPresenter() {}

    public static GqlCategory present(final ListCategoryOutput out) {
        return new GqlCategory(out.id(), out.name(), out.description());
    }

    public static GqlCategory present(final GetAllCategoriesByIdUseCase.Output out) {
        return new GqlCategory(out.id(), out.name(), out.description());
    }

    public static GqlCategory present(final Category out) {
        return new GqlCategory(out.id(), out.name(), out.description());
    }
}
