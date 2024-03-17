package com.fullcycle.catalogo.domain.category;

import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category save(Category aCategory);

    void deleteById(String anId);

    Optional<Category> findById(String anId);

    List<Category> findAllById(List<String> ids);

    Pagination<Category> findAll(CategorySearchQuery aQuery);

}
