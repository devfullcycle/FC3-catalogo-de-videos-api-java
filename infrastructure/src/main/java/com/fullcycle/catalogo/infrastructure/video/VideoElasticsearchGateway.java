package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class VideoElasticsearchGateway implements VideoGateway {

    private final VideoRepository videoRepository;
    private final SearchOperations searchOperations;

    public VideoElasticsearchGateway(final VideoRepository videoRepository, final SearchOperations searchOperations) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Video save(final Video video) {
        this.videoRepository.save(VideoDocument.from(video));
        return video;
    }

    @Override
    public void deleteById(final String videoId) {
        this.videoRepository.deleteById(videoId);
    }

    @Override
    public Optional<Video> findById(final String videoId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return null;
    }
}
