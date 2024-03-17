package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import com.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class GenreInMemoryGateway implements GenreGateway {

    private final Map<String, Genre> db;

    public GenreInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Genre save(final Genre aGenre) {
        this.db.put(aGenre.id(), aGenre);
        return aGenre;
    }

    @Override
    public void deleteById(String genreId) {
        this.db.remove(genreId);
    }

    @Override
    public Optional<Genre> findById(String genreId) {
        return Optional.ofNullable(this.db.get(genreId));
    }

    @Override
    public List<Genre> findAllById(List<String> genreId) {
        if (genreId == null || genreId.isEmpty()) {
            return List.of();
        }
        return genreId.stream()
                .map(this.db::get)
                .toList();
    }

    @Override
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()
        );
    }
}
