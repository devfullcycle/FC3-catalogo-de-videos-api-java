package com.fullcycle.catalogo.infrastructure.castmember.models;

import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record GqlCastMemberInput(
        String id,
        String name,
        String type,
        Instant createdAt,
        Instant updatedAt
) {
    public CastMember toCastMember() {
        return CastMember.with(id, name, CastMemberType.of(type), createdAt, updatedAt);
    }
}
