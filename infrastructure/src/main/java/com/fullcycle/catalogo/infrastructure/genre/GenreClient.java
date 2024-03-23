package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.infrastructure.genre.models.GenreDTO;

import java.util.Optional;

public interface GenreClient {

    Optional<GenreDTO> genreOfId(String genreId);

}
