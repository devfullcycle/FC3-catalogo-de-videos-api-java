package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class CategoryInMemoryGateway implements CategoryGateway {

    private final Map<String, Category> db;

    public CategoryInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Category save(final Category aCategory) {
        this.db.put(aCategory.id(), aCategory);
        return aCategory;
    }

    @Override
    public void deleteById(String genreId) {
        this.db.remove(genreId);
    }

    @Override
    public Optional<Category> findById(String genreId) {
        return Optional.ofNullable(this.db.get(genreId));
    }

    @Override
    public List<Category> findAllById(Set<String> genreId) {
        if (genreId == null || genreId.isEmpty()) {
            return List.of();
        }
        return genreId.stream()
                .map(this.db::get)
                .toList();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()
        );
    }
}
