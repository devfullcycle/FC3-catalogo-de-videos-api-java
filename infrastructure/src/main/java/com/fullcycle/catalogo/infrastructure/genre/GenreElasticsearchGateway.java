package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import com.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.data.elasticsearch.core.query.Criteria.where;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Profile("!development")
public class GenreElasticsearchGateway implements GenreGateway {

    public static final String NAME_PROP = "name";
    public static final String KEYWORD_SUFIX = ".keyword";
    private final GenreRepository genreRepository;
    private final SearchOperations searchOperations;

    public GenreElasticsearchGateway(
            final GenreRepository genreRepository,
            final SearchOperations searchOperations
    ) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Genre save(final Genre aGenre) {
        this.genreRepository.save(GenreDocument.from(aGenre));
        return aGenre;
    }

    @Override
    public void deleteById(final String genreId) {
        this.genreRepository.deleteById(genreId);
    }

    @Override
    public Optional<Genre> findById(final String genreId) {
        return this.genreRepository.findById(genreId)
                .map(GenreDocument::toGenre);
    }

    @Override
    public List<Genre> findAllById(final Set<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return StreamSupport.stream(this.genreRepository.findAllById(ids).spliterator(), false)
                .map(GenreDocument::toGenre)
                .toList();
    }

    @Override
    public Pagination<Genre> findAll(final GenreSearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var itemsPerPage = aQuery.perPage();
        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var pageRequest = PageRequest.of(currentPage, itemsPerPage, sort);

        final Query query = StringUtils.isEmpty(terms) && isEmpty(aQuery.categories())
                ? Query.findAll().setPageable(pageRequest)
                : new CriteriaQuery(createCriteria(aQuery), pageRequest);

        final var res = this.searchOperations.search(query, GenreDocument.class);
        final var total = res.getTotalHits();
        final var genres = res.stream()
                .map(SearchHit::getContent)
                .map(GenreDocument::toGenre)
                .toList();

        return new Pagination<>(currentPage, itemsPerPage, total, genres);
    }

    private static Criteria createCriteria(final GenreSearchQuery aQuery) {
        Criteria criteria = null;

        if (isNotEmpty(aQuery.terms())) {
            criteria = where("name").contains(aQuery.terms());
        }

        if (!isEmpty(aQuery.categories())) {
            final var categoriesWhere = where("categories").in(aQuery.categories());
            criteria = criteria != null ? criteria.and(categoriesWhere) : categoriesWhere;
        }

        return criteria;
    }

    private String buildSort(final String sort) {
        if (NAME_PROP.equalsIgnoreCase(sort)) {
            return sort.concat(KEYWORD_SUFIX);
        } else {
            return sort;
        }
    }
}
