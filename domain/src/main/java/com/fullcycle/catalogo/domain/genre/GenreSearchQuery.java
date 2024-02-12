package com.fullcycle.catalogo.domain.genre;

import java.util.Set;

public record GenreSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<String> categories
) {

    @Override
    public String terms() {
        return terms != null ? terms : "";
    }

    @Override
    public String sort() {
        return sort != null ? sort : "";
    }

    @Override
    public String direction() {
        return direction != null ? direction : "";
    }

    @Override
    public Set<String> categories() {
        return categories != null ? categories : Set.of();
    }
}
