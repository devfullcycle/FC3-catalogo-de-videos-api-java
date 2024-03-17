package com.fullcycle.catalogo.infrastructure.castmember.models;

public record GqlCastMember(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt
) {
}
