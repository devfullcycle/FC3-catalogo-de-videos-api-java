package com.fullcycle.catalogo.application.video.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class GetVideoUseCase extends UseCase<GetVideoUseCase.Input, Optional<GetVideoUseCase.Output>> {

    private final VideoGateway videoGateway;

    public GetVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        if (input == null || input.videoId() == null) {
            return Optional.empty();
        }

        return this.videoGateway.findById(input.videoId())
                .map(Output::from);
    }

    public record Input(String videoId) {

    }

    public record Output(
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

        public static Output from(final Video video) {
            return new Output(
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
    }
}
