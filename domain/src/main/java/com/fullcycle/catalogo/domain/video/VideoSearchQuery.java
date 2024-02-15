package com.fullcycle.catalogo.domain.video;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        String rating,
        Integer launchedAt,
        Set<String> categories,
        Set<String> castMembers,
        Set<String> genres
) {

    public boolean hasFilter() {
        return !terms().isBlank() || launchedAt != null || !rating().isBlank() ||
                !categories().isEmpty() || !castMembers().isEmpty() || !genres().isEmpty();
    }

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
    public String rating() {
        return rating != null ? rating : "";
    }

    @Override
    public Set<String> categories() {
        return categories != null ? categories : Set.of();
    }

    @Override
    public Set<String> castMembers() {
        return castMembers != null ? castMembers : Set.of();
    }

    @Override
    public Set<String> genres() {
        return genres != null ? genres : Set.of();
    }
}
