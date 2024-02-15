package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.data.elasticsearch.core.query.Criteria.where;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class VideoElasticsearchGateway implements VideoGateway {

    private static final String TITLE_PROP = "title";
    private static final String KEYWORD_SUFIX = ".keyword";

    private final VideoRepository videoRepository;
    private final SearchOperations searchOperations;

    public VideoElasticsearchGateway(final VideoRepository videoRepository, final SearchOperations searchOperations) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Video save(final Video video) {
        this.videoRepository.save(VideoDocument.from(video));
        return video;
    }

    @Override
    public void deleteById(final String videoId) {
        this.videoRepository.deleteById(videoId);
    }

    @Override
    public Optional<Video> findById(final String videoId) {
        return this.videoRepository.findById(videoId)
                .map(VideoDocument::toVideo);
    }

    @Override
    public Pagination<Video> findAll(final VideoSearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var itemsPerPage = aQuery.perPage();
        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var pageRequest = PageRequest.of(currentPage, itemsPerPage, sort);

        final Query query = StringUtils.isEmpty(terms) && isEmpty(aQuery.categories())
                ? Query.findAll().setPageable(pageRequest)
                : new CriteriaQuery(createCriteria(aQuery), pageRequest);

        final var res = this.searchOperations.search(query, VideoDocument.class);
        final var total = res.getTotalHits();
        final var genres = res.stream()
                .map(SearchHit::getContent)
                .map(VideoDocument::toVideo)
                .toList();

        return new Pagination<>(currentPage, itemsPerPage, total, genres);
    }

    private static Criteria createCriteria(final VideoSearchQuery aQuery) {
        final var criteria = new Criteria();

        if (isNotEmpty(aQuery.terms())) {
            criteria.and(
                    where("title").contains(aQuery.terms())
                            .or(where("description").contains(aQuery.terms()))
            );
        }

        if (isNotEmpty(aQuery.rating())) {
            criteria.and(where("rating").is(aQuery.rating()));
        }

        if (Objects.nonNull(aQuery.launchedAt())) {
            criteria.and(where("launchedAt").is(aQuery.launchedAt()));
        }

        if (!isEmpty(aQuery.categories())) {
            criteria.and(where("categories").in(aQuery.categories()));
        }

        if (!isEmpty(aQuery.castMembers())) {
            criteria.and(where("castMembers()").in(aQuery.castMembers()));
        }

        if (!isEmpty(aQuery.genres())) {
            criteria.and(where("genres()").in(aQuery.genres()));
        }

        return criteria;
    }

    private String buildSort(final String sort) {
        if (TITLE_PROP.equalsIgnoreCase(sort)) {
            return sort.concat(KEYWORD_SUFIX);
        } else {
            return sort;
        }
    }
}
