package com.fullcycle.catalogo.infrastructure.video.models;

import java.util.Collections;
import java.util.Set;

public record GqlVideoInput(
        String id,
        String title,
        String description,
        Integer yearLaunched,
        String rating,
        double duration,
        Boolean opened,
        Boolean published,
        String video,
        String trailer,
        String banner,
        String thumbnail,
        String thumbnailHalf,
        Set<String> castMembersId,
        Set<String> categoriesId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {

    @Override
    public Boolean opened() {
        return opened != null ? opened : false;
    }

    @Override
    public Boolean published() {
        return published != null ? published : false;
    }

    @Override
    public Set<String> castMembersId() {
        return castMembersId != null ? castMembersId : Collections.emptySet();
    }

    @Override
    public Set<String> categoriesId() {
        return categoriesId != null ? categoriesId : Collections.emptySet();
    }

    @Override
    public Set<String> genresId() {
        return genresId != null ? genresId : Collections.emptySet();
    }
}
