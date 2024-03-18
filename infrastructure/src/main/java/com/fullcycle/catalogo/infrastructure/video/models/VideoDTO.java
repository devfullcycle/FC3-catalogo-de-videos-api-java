package com.fullcycle.catalogo.infrastructure.video.models;

import java.util.Set;

public record VideoDTO(
        String id,
        String title,
        String description,
        int yearLaunched,
        String rating,
        Double duration,
        boolean opened,
        boolean published,
        String video,
        String trailer,
        String banner,
        String thumbnail,
        String thumbnailHalf,
        Set<String> categoriesId,
        Set<String> castMembersId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {
}
