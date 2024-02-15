package com.fullcycle.catalogo.infrastructure.genre.persistence;

import com.fullcycle.catalogo.domain.genre.Genre;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.util.Set;

@Document(indexName = "genres")
public class GenreDocument {

    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "name"),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword)
    )
    private String name;

    @Field(type = FieldType.Boolean, name = "isActive")
    private boolean active;

    @Field(type = FieldType.Keyword, name = "categories")
    private Set<String> categories;

    @Field(type = FieldType.Date, name = "created_at")
    private Instant createdAt;

    @Field(type = FieldType.Date, name = "updated_at")
    private Instant updatedAt;

    @Field(type = FieldType.Date, name = "deleted_at")
    private Instant deletedAt;

    public GenreDocument(
            final String id,
            final String name,
            final boolean active,
            final Set<String> categories,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreDocument from(final Genre aGenre) {
        return new GenreDocument(
                aGenre.id(),
                aGenre.name(),
                aGenre.active(),
                aGenre.categories(),
                aGenre.createdAt(),
                aGenre.updatedAt(),
                aGenre.deletedAt()
        );
    }

    public Genre toGenre() {
        return Genre.with(
                id(),
                name(),
                active(),
                categories(),
                createdAt(),
                updatedAt(),
                deletedAt()
        );
    }

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> categories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public boolean active() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
