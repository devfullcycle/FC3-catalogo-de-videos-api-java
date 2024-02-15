package com.fullcycle.catalogo.application.video.save;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.validation.Error;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;

import java.util.Objects;
import java.util.Set;

public class SaveVideoUseCase extends UseCase<SaveVideoUseCase.Input, SaveVideoUseCase.Output> {

    private final VideoGateway videoGateway;

    public SaveVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw DomainException.with(new Error("'SaveVideoUseCase.Input' cannot be null"));
        }

        final var video = this.videoGateway.save(Video.with(
                input.id(),
                input.title(),
                input.description(),
                input.launchedAt(),
                input.duration(),
                input.rating(),
                input.opened(),
                input.published(),
                input.createdAt(),
                input.updatedAt(),
                input.video(),
                input.trailer(),
                input.banner(),
                input.thumbnail(),
                input.thumbnailHalf(),
                input.categories(),
                input.castMembers(),
                input.genres()
        ));

        return new Output(video.id());
    }

    public record Input(
            String id,
            String title,
            String description,
            Integer launchedAt,
            double duration,
            String rating,
            boolean opened,
            boolean published,
            String createdAt,
            String updatedAt,
            String video,
            String trailer,
            String banner,
            String thumbnail,
            String thumbnailHalf,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ) {
    }

    public record Output(String id) {
    }
}
