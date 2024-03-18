package com.fullcycle.catalogo.infrastructure.video;

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

import java.util.Objects;
import java.util.Optional;

import static com.fullcycle.catalogo.infrastructure.video.VideoQueryBuilder.*;

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

        final var aQueryBuilder = new VideoQueryBuilder(
                onlyPublished(),
                containingCastMembers(aQuery.castMembers()),
                containingCategories(aQuery.categories()),
                containingGenres(aQuery.genres()),
                launchedAtEquals(aQuery.launchedAt()),
                ratingEquals(aQuery.rating()),
                titleOrDescriptionContaining(aQuery.terms())
        );

        final var query = NativeQuery.builder()
                .withQuery(aQueryBuilder.build())
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

    private String buildSort(final String sort) {
        if (TITLE_PROP.equalsIgnoreCase(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }
}
