package com.fullcycle.catalogo.infrastructure.video.models;

import com.fullcycle.catalogo.domain.video.Video;

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
    public static VideoDTO from(final Video aVideo) {
        return new VideoDTO(
                aVideo.id(),
                aVideo.title(),
                aVideo.description(),
                aVideo.launchedAt().getValue(),
                aVideo.rating().getName(),
                aVideo.duration(),
                aVideo.opened(),
                aVideo.published(),
                aVideo.video(),
                aVideo.trailer(),
                aVideo.banner(),
                aVideo.thumbnail(),
                aVideo.thumbnailHalf(),
                aVideo.categories(),
                aVideo.castMembers(),
                aVideo.genres(),
                aVideo.createdAt().toString(),
                aVideo.updatedAt().toString()
        );
    }
}
