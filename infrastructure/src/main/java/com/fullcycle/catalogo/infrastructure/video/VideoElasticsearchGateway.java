package com.fullcycle.catalogo.infrastructure.video;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Profile("!development")
public class VideoElasticsearchGateway implements VideoGateway {

    private static final String TITLE_PROP = "title";
    private static final String KEYWORD = ".keyword";

    private final SearchOperations searchOperations;
    private final VideoRepository videoRepository;

    public VideoElasticsearchGateway(final SearchOperations searchOperations, final VideoRepository videoRepository) {
        this.searchOperations = Objects.requireNonNull(searchOperations);
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    public Video save(final Video video) {
        this.videoRepository.save(VideoDocument.from(video));
        return video;
    }

    @Override
    public void deleteById(final String videoId) {
        if (videoId == null || videoId.isBlank()) {
            return;
        }

        this.videoRepository.deleteById(videoId);
    }

    @Override
    public Optional<Video> findById(final String videoId) {
        if (videoId == null || videoId.isBlank()) {
            return Optional.empty();
        }

        return this.videoRepository.findById(videoId)
                .map(VideoDocument::toVideo);
    }

    @Override
    public Pagination<Video> findAll(final VideoSearchQuery aQuery) {
        final var currentPage = aQuery.page();
        final var itemsPerPage = aQuery.perPage();

        final List<Query> must = new ArrayList<>();
        must.add(QueryBuilders.term(t -> t.field("published").value(true)));

        if (!isEmpty(aQuery.castMembers())) {
            must.add(QueryBuilders.terms(t -> t.field("cast_members").terms(it -> it.value(fieldValues(aQuery.castMembers())))));
        }

        if (!isEmpty(aQuery.categories())) {
            must.add(QueryBuilders.terms(t -> t.field("categories").terms(it -> it.value(fieldValues(aQuery.categories())))));
        }

        if (!isEmpty(aQuery.genres())) {
            must.add(QueryBuilders.terms(t -> t.field("genres").terms(it -> it.value(fieldValues(aQuery.genres())))));
        }

        if (aQuery.launchedAt() != null) {
            must.add(QueryBuilders.term(t -> t.field("launched_at").value(aQuery.launchedAt())));
        }

        if (aQuery.rating() != null && !aQuery.rating().isBlank()) {
            must.add(QueryBuilders.term(t -> t.field("rating").value(aQuery.rating())));
        }

        if (aQuery.terms() != null && !aQuery.terms().isBlank()) {
            must.add(QueryBuilders.queryString(q -> q.fields("title", "description").query("*" + aQuery.terms() + "*")));
        }

        final var query = NativeQuery.builder()
                .withQuery(QueryBuilders.bool(b -> b.must(must)))
                .withPageable(PageRequest.of(currentPage, itemsPerPage, Sort.by(Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()))))
                .build();

        final var res = this.searchOperations.search(query, VideoDocument.class);
        final var total = res.getTotalHits();
        final var videos = res.stream()
                .map(SearchHit::getContent)
                .map(VideoDocument::toVideo)
                .toList();

        return new Pagination<>(currentPage, itemsPerPage, total, videos);
    }

    private List<FieldValue> fieldValues(final Set<String> ids) {
        return ids.stream()
                .map(FieldValue::of)
                .toList();
    }

    private String buildSort(final String sort) {
        if (TITLE_PROP.equalsIgnoreCase(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }
}
