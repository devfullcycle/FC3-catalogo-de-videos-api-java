package com.fullcycle.catalogo.infrastructure.category.models;

import com.fullcycle.catalogo.domain.category.Category;

import java.time.Instant;

public record GqlCategoryInput(
        String id,
        String name,
        String description,
        Boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    @Override
    public Boolean active() {
        return active != null ? active : true;
    }

    public Category toCategory() {
        return Category.with(id(), name(), description(), active(), createdAt(), updatedAt(), deletedAt());
    }
}
