package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import com.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
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
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {
        return null;
    }
}
