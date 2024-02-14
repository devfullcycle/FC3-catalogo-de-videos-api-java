package com.fullcycle.catalogo.domain.video;

import com.fullcycle.catalogo.domain.validation.Error;
import com.fullcycle.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Set;

public class Video {

    private String id;
    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private String banner;
    private String thumbnail;
    private String thumbnailHalf;

    private String trailer;
    private String video;

    private Set<String> categories;
    private Set<String> genres;
    private Set<String> castMembers;

    private Video(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String video,
            final String trailer,
            final String banner,
            final String thumbnail,
            final String thumbnailHalf,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.launchedAt = Year.of(launchedAt);
        this.duration = duration;
        this.rating = Rating.of(rating).orElse(null);
        this.opened = opened;
        this.published = published;
        this.createdAt = Instant.parse(createdAt);
        this.updatedAt = Instant.parse(updatedAt);
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = categories != null ? categories : Set.of();
        this.genres = genres != null ? genres : Set.of();
        this.castMembers = castMembers != null ? castMembers : Set.of();

        validate(new ThrowsValidationHandler());

        if (video == null || video.isBlank()) {
            this.published = false;
        }

        if (trailer == null || trailer.isBlank()) {
            this.published = false;
        }

        if (banner == null || banner.isBlank()) {
            this.published = false;
        }

        if (thumbnail == null || thumbnail.isBlank()) {
            this.published = false;
        }

        if (thumbnailHalf == null || thumbnailHalf.isBlank()) {
            this.published = false;
        }
    }

    public static Video with(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String video,
            final String trailer,
            final String banner,
            final String thumbnail,
            final String thumbnailHalf,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres
    ) {
        return new Video(
                id,
                title,
                description,
                launchedAt,
                duration,
                rating,
                opened,
                published,
                createdAt,
                updatedAt,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf,
                categories,
                castMembers,
                genres
        );
    }

    public static Video with(final Video video) {
        return with(
                video.id(),
                video.title(),
                video.description(),
                video.launchedAt().getValue(),
                video.duration(),
                video.rating().getName(),
                video.opened(),
                video.published(),
                video.createdAt().toString(),
                video.updatedAt().toString(),
                video.video(),
                video.trailer(),
                video.banner(),
                video.thumbnail(),
                video.thumbnailHalf(),
                video.categories(),
                video.castMembers(),
                video.genres()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (title == null || title.isBlank()) {
            handler.append(new Error("'title' should not be empty"));
        }

        if (launchedAt == null) {
            handler.append(new Error("'launchedAt' should not be empty"));
        }

        if (rating == null) {
            handler.append(new Error("'rating' should not be empty"));
        }

        if (createdAt == null) {
            handler.append(new Error("'createdAt' should not be empty"));
        }

        if (updatedAt == null) {
            handler.append(new Error("'updatedAt' should not be empty"));
        }
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Year launchedAt() {
        return launchedAt;
    }

    public double duration() {
        return duration;
    }

    public Rating rating() {
        return rating;
    }

    public boolean opened() {
        return opened;
    }

    public boolean published() {
        return published;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public String banner() {
        return banner;
    }

    public String thumbnail() {
        return thumbnail;
    }

    public String thumbnailHalf() {
        return thumbnailHalf;
    }

    public String trailer() {
        return trailer;
    }

    public String video() {
        return video;
    }

    public Set<String> categories() {
        return categories;
    }

    public Set<String> genres() {
        return genres;
    }

    public Set<String> castMembers() {
        return castMembers;
    }
}
