package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.infrastructure.video.models.VideoDTO;

import java.util.Optional;

public interface VideoClient {
    Optional<VideoDTO> videoOfId(String videoId);
}
