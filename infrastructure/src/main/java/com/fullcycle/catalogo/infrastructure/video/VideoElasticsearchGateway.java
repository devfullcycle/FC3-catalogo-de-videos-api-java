package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoQueryBuilder;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.fullcycle.catalogo.infrastructure.video.persistence.VideoQueryBuilder.*;

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
        final var currentPage = aQuery.page();
        final var itemsPerPage = aQuery.perPage();

        final var queryB = new VideoQueryBuilder(
                onlyPublished(),
                containingGenres(aQuery.genres()),
                containingCategories(aQuery.categories()),
                containingCastMembers(aQuery.castMembers()),
                withRating(aQuery.rating()),
                withTitleOrDescriptionHaving(aQuery.terms()),
                launchedAt(aQuery.launchedAt())
        );

        final var query = NativeQuery.builder()
                .withQuery(queryB.build())
                .withPageable(PageRequest.of(
                        currentPage,
                        itemsPerPage,
                        Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()))
                ))
                .build();

        final var res = this.searchOperations.search(query, VideoDocument.class);
        final var total = res.getTotalHits();
        final var videos = res.stream()
                .map(SearchHit::getContent)
                .map(VideoDocument::toVideo)
                .toList();

        return new Pagination<>(currentPage, itemsPerPage, total, videos);
    }

    private String buildSort(final String sort) {
        if (TITLE_PROP.equalsIgnoreCase(sort)) {
            return sort.concat(KEYWORD_SUFIX);
        } else {
            return sort;
        }
    }
}
