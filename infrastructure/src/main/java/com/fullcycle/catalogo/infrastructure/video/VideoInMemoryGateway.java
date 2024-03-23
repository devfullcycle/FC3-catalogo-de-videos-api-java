package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VideoInMemoryGateway implements VideoGateway {

    private final Map<String, Video> db;

    public VideoInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Video save(Video video) {
        this.db.put(video.id(), video);
        return video;
    }

    @Override
    public void deleteById(String videoId) {
        this.db.remove(videoId);
    }

    @Override
    public Optional<Video> findById(String videoId) {
        return Optional.ofNullable(this.db.get(videoId));
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return new Pagination<>(aQuery.page(), aQuery.perPage(), this.db.size(), this.db.values().stream().toList());
    }
}
