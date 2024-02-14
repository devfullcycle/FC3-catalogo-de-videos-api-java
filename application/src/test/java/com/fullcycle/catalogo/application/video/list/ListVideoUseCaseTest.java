package com.fullcycle.catalogo.application.video.list;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidQuery_whenCallsListVideos_shouldReturnIt() {
        // given
        final var videos = List.of(
                Fixture.Videos.systemDesign(),
                Fixture.Videos.java21()
        );

        final var expectedItems = videos.stream()
                .map(ListVideoUseCase.Output::from)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedYear = 2022;
        final var expectedRating = "L";
        final var expectedCategories = Set.of("c1");
        final var expectedCastMembers = Set.of("c1");
        final var expectedGenres = Set.of("c1");

        final var aQuery = new ListVideoUseCase.Input(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                expectedRating,
                expectedYear,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, videos.size(), videos);

        when(this.videoGateway.findAll(any()))
                .thenReturn(pagination);

        // when
        final var actualOutput = this.useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedItemsCount, actualOutput.meta().total());
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.data().size() &&
                        expectedItems.containsAll(actualOutput.data())
        );
    }
}
