package com.fullcycle.catalogo.infrastructure.video.persistence;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.springframework.util.CollectionUtils.isEmpty;

public class VideoQueryFactory {

    private final VideoSearchQuery aQuery;
    private final List<Query> must;

    public VideoQueryFactory(final VideoSearchQuery aQuery) {
        this.aQuery = aQuery;
        this.must = new ArrayList<>();
    }

    public Query createQuery() {
        onlyPublished();
        withTitleOrDescriptionHaving();
        withRatingEquals();
        launchedAt();
        withinCategories();
        withinCastMembers();
        withinGenres();
        return build();
    }

    private Query build() {
        return QueryBuilders.bool(bool -> {
            return bool.must(must);
        });
    }

    private void withinGenres() {
        if (!isEmpty(aQuery.genres())) {
            must.add(QueryBuilders.terms(t ->
                    t.field("genres").terms(it -> it.value(fieldValues(aQuery.genres())))
            ));
        }
    }

    private void withinCastMembers() {
        if (!isEmpty(aQuery.castMembers())) {
            must.add(QueryBuilders.terms(t ->
                    t.field("cast_members").terms(it -> it.value(fieldValues(aQuery.castMembers())))
            ));
        }
    }

    private void withinCategories() {
        if (!isEmpty(aQuery.categories())) {
            must.add(QueryBuilders.terms(t ->
                    t.field("categories").terms(it -> it.value(fieldValues(aQuery.categories())))
            ));
        }
    }

    private void launchedAt() {
        if (Objects.nonNull(aQuery.launchedAt())) {
            must.add(QueryBuilders.term(t -> t.field("launched_at").value(aQuery.launchedAt())));
        }
    }

    private void withRatingEquals() {
        if (!aQuery.rating().isBlank()) {
            must.add(QueryBuilders.term(t -> t.field("rating").value(aQuery.rating())));
        }
    }

    private void withTitleOrDescriptionHaving() {
        if (!aQuery.terms().isBlank()) {
            must.add(QueryBuilders.queryString(t -> t.fields("title", "description").query("*" + aQuery.terms() + "*")));
        }
    }

    private void onlyPublished() {
        must.add(QueryBuilders.term(t -> t.field("published").value(true)));
    }

    private static List<FieldValue> fieldValues(final Collection<String> ids) {
        return ids.stream()
                .map(FieldValue::of)
                .toList();
    }
}
