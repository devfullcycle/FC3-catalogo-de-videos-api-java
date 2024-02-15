package com.fullcycle.catalogo.infrastructure.video.persistence;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.springframework.util.CollectionUtils.isEmpty;

public class VideoQueryBuilder {

    public static final Option NOOP_OPTION = b -> {
    };

    private final List<Query> must;

    public interface Option extends Consumer<VideoQueryBuilder> {
    }

    public VideoQueryBuilder(final Option... opts) {
        this.must = new ArrayList<>();

        for (Option op : opts) {
            op.accept(this);
        }
    }

    public static Option onlyPublished() {
        return b -> b.must.add(QueryBuilders.term(t -> t.field("published").value(true)));
    }

    public Query build() {
        return QueryBuilders.bool(bool -> {
            return bool.must(must);
        });
    }

    public static Option containingGenres(final Set<String> genres) {
        if (isEmpty(genres)) {
            return NOOP_OPTION;
        }

        return b -> b.must.add(QueryBuilders.terms(t -> t.field("genres").terms(it -> it.value(fieldValues(genres)))));
    }

    public static Option containingCastMembers(final Set<String> castMembers) {
        if (isEmpty(castMembers)) {
            return NOOP_OPTION;
        }

        return b -> b.must.add(QueryBuilders.terms(t -> t.field("cast_members").terms(it -> it.value(fieldValues(castMembers)))));
    }

    public static Option containingCategories(final Set<String> categories) {
        if (isEmpty(categories)) {
            return NOOP_OPTION;
        }

        return b -> b.must.add(QueryBuilders.terms(t -> t.field("categories").terms(it -> it.value(fieldValues(categories)))));
    }

    public static Option launchedAt(final Integer year) {
        if (year == null) {
            return NOOP_OPTION;
        }

        return b -> b.must.add(QueryBuilders.term(t -> t.field("launched_at").value(year)));
    }

    public static Option withRating(final String rating) {
        if (rating == null || rating.isBlank()) {
            return NOOP_OPTION;
        }

        return b -> b.must.add(QueryBuilders.term(t -> t.field("rating").value(rating)));
    }

    public static Option withTitleOrDescriptionHaving(final String q) {
        if (q == null || q.isBlank()) {
            return NOOP_OPTION;
        }

        return b -> b.must.add(QueryBuilders.queryString(t -> t.fields("title", "description").query("*" + q + "*")));
    }

    private static List<FieldValue> fieldValues(final Collection<String> ids) {
        return ids.stream()
                .map(FieldValue::of)
                .toList();
    }
}
