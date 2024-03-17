package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Profile("!development")
public class VideoElasticsearchGateway implements VideoGateway {

    private final SearchOperations searchOperations;
    private final VideoRepository videoRepository;

    public VideoElasticsearchGateway(final SearchOperations searchOperations, final VideoRepository videoRepository) {
        this.searchOperations = Objects.requireNonNull(searchOperations);
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    public Video save(final Video video) {
        this.videoRepository.save(VideoDocument.from(video));
        return video;
    }

    @Override
    public void deleteById(final String videoId) {
        if (videoId == null || videoId.isBlank()) {
            return;
        }

        this.videoRepository.deleteById(videoId);
    }

    @Override
    public Optional<Video> findById(String videoId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return null;
    }
}
