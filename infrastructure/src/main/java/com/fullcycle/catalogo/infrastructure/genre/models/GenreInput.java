package com.fullcycle.catalogo.infrastructure.genre.models;

import com.fullcycle.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.Set;

public record GenreInput(
        String id,
        String name,
        Boolean active,
        Set<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GenreInput from(final Genre genre) {
        return new GenreInput(
                genre.id(),
                genre.name(),
                genre.active(),
                genre.categories(),
                genre.createdAt(),
                genre.updatedAt(),
                genre.deletedAt()
        );
    }
}
