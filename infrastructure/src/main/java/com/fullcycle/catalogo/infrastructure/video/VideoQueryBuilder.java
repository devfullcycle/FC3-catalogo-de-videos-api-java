package com.fullcycle.catalogo.infrastructure.video;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.springframework.util.CollectionUtils.isEmpty;

public final class VideoQueryBuilder {

    private static final Option NOOP = b -> {};

    private final List<Query> must;

    public VideoQueryBuilder(final Option... opts) {
        this.must = new ArrayList<>();

        for (final Option opt : opts) {
            opt.accept(this);
        }
    }

    public VideoQueryBuilder must(final Query aQuery) {
        this.must.add(aQuery);
        return this;
    }

    public Query build() {
        return QueryBuilders.bool(b -> b.must(must));
    }

    public interface Option extends Consumer<VideoQueryBuilder> {

    }

    public static Option onlyPublished() {
        return b -> b.must(QueryBuilders.term(t -> t.field("published").value(true)));
    }

    public static Option containingCastMembers(final Set<String> members) {
        if (isEmpty(members)) {
            return NOOP;
        }

        return b -> b.must(QueryBuilders.terms(t -> t.field("cast_members").terms(it -> it.value(fieldValues(members)))));
    }

    public static Option containingCategories(final Set<String> categories) {
        if (isEmpty(categories)) {
            return NOOP;
        }

        return b -> b.must(QueryBuilders.terms(t -> t.field("categories").terms(it -> it.value(fieldValues(categories)))));
    }

    public static Option containingGenres(final Set<String> genres) {
        if (isEmpty(genres)) {
            return NOOP;
        }

        return b -> b.must(QueryBuilders.terms(t -> t.field("genres").terms(it -> it.value(fieldValues(genres)))));
    }

    public static Option launchedAtEquals(final Integer launchedAt) {
        if (launchedAt == null) {
            return NOOP;
        }

        return b -> b.must(QueryBuilders.term(t -> t.field("launched_at").value(launchedAt)));
    }

    public static Option ratingEquals(final String rating) {
        if (rating == null || rating.isBlank()) {
            return NOOP;
        }

        return b -> b.must(QueryBuilders.term(t -> t.field("rating").value(rating)));
    }

    public static Option titleOrDescriptionContaining(final String terms) {
        if (terms == null || terms.isBlank()) {
            return NOOP;
        }

        return b -> b.must(QueryBuilders.queryString(q -> q.fields("title", "description").query("*" + terms + "*")));
    }

    private static List<FieldValue> fieldValues(final Set<String> ids) {
        return ids.stream()
                .map(FieldValue::of)
                .toList();
    }
}
