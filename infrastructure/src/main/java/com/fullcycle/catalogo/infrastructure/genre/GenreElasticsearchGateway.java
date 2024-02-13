package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import com.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreElasticsearchGateway implements GenreGateway {

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
    public Genre save(Genre aGenre) {
        return null;
    }

    @Override
    public void deleteById(String genreId) {

    }

    @Override
    public Optional<Genre> findById(String genreId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {
        return null;
    }
}
