package com.fullcycle.catalogo.domain.genre;

import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface GenreGateway {

    Genre save(Genre aGenre);

    void deleteById(String genreId);

    Optional<Genre> findById(String genreId);

    List<Genre> findAllById(List<String> genreId);

    Pagination<Genre> findAll(GenreSearchQuery aQuery);

}
