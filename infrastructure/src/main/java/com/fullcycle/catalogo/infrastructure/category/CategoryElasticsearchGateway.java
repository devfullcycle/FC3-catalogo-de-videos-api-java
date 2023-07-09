package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.elasticsearch.core.query.Criteria.where;

@Component
public class CategoryElasticsearchGateway implements CategoryGateway {

    private static final String NAME_PROP = "name";
    private static final String KEYWORD = ".keyword";

    private final CategoryRepository categoryRepository;
    private final SearchOperations searchOperations;

    public CategoryElasticsearchGateway(
            final CategoryRepository categoryRepository,
            final SearchOperations searchOperations
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Category save(final Category aCategory) {
        this.categoryRepository.save(CategoryDocument.from(aCategory));
        return aCategory;
    }

    @Override
    public void deleteById(final String anId) {
        this.categoryRepository.deleteById(anId);
    }

    @Override
    public Optional<Category> findById(final String anId) {
        return this.categoryRepository.findById(anId)
                .map(CategoryDocument::toCategory);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, perPage, sort);

        final Query query;
        if (StringUtils.isNotEmpty(terms)) {
            final var criteria = where("name").contains(terms)
                    .or(where("description").contains(terms));

            query = new CriteriaQuery(criteria, page);
        } else {
            query = Query.findAll().setPageable(page);
        }

        final var res = this.searchOperations.search(query, CategoryDocument.class);

        final var total = res.getTotalHits();

        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(CategoryDocument::toCategory)
                .toList();

        return new Pagination<>(currentPage, perPage, total, categories);
    }

    private String buildSort(final String sort) {
        if (NAME_PROP.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }
}
