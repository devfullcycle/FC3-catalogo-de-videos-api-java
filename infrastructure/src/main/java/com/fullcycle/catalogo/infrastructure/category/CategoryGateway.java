package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.domain.category.Category;

import java.util.Optional;

public interface CategoryGateway {

    Optional<Category> categoryOfId(String anId);
}
