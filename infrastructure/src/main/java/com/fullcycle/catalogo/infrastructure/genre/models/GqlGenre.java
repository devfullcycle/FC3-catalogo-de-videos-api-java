package com.fullcycle.catalogo.infrastructure.genre.models;

import java.util.Set;

public record GqlGenre(
        String id,
        String name,
        Boolean active,
        Set<String> categories,
        String createdAt,
        String updatedAt,
        String deletedAt
) {
}
