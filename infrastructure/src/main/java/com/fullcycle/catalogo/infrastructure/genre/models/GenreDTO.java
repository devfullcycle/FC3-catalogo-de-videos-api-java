package com.fullcycle.catalogo.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record GenreDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("active") Boolean active,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
