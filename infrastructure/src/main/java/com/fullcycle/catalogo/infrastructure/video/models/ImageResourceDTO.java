package com.fullcycle.catalogo.infrastructure.video.models;

public record ImageResourceDTO(
        String id,
        String name,
        String checksum,
        String location
) {
}
