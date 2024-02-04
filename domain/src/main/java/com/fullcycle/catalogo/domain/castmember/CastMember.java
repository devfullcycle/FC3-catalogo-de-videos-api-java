package com.fullcycle.catalogo.domain.castmember;

import com.fullcycle.catalogo.domain.validation.Error;
import com.fullcycle.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class CastMember {

    private String id;
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    private CastMember(
            final String id,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMember with(
            final String id,
            final String name,
            final CastMemberType type,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        return new CastMember(id, name, type, createdAt, updatedAt);
    }

    public static CastMember with(final CastMember castMember) {
        return with(
                castMember.id(),
                castMember.name(),
                castMember.type(),
                castMember.createdAt(),
                castMember.updatedAt()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            handler.append(new Error("'name' should not be empty"));
        }

        if (type == null) {
            handler.append(new Error("'type' should not be null"));
        }
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public CastMemberType type() {
        return type;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
