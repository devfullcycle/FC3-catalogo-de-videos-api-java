package com.fullcycle.catalogo.infrastructure.genre.models;

import java.time.Instant;
import java.util.Set;

public record GenreDTO(
        String id,
        String name,
        Boolean active,
        Set<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
}
