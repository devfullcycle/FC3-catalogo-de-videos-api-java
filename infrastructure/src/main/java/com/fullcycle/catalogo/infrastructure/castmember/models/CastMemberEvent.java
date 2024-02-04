package com.fullcycle.catalogo.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberEvent(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("createdAt") String createdAt,
        @JsonProperty("updatedAt") String updatedAt
) {
    public static CastMemberEvent from(final CastMember aMember) {
        return new CastMemberEvent(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

    public CastMember toCastMember() {
        return CastMember.with(id, name, CastMemberType.of(type), Instant.parse(createdAt), Instant.parse(updatedAt));
    }
}
