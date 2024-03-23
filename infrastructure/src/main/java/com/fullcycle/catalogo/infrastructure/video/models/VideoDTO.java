package com.fullcycle.catalogo.infrastructure.video.models;

import java.util.Optional;
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
        VideoResourceDTO video,
        VideoResourceDTO trailer,
        ImageResourceDTO banner,
        ImageResourceDTO thumbnail,
        ImageResourceDTO thumbnailHalf,
        Set<String> categoriesId,
        Set<String> castMembersId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {

    public Optional<VideoResourceDTO> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<VideoResourceDTO> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<ImageResourceDTO> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageResourceDTO> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageResourceDTO> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
}
