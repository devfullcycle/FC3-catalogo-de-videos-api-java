package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.GraphQLControllerTest;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = VideoGraphQLController.class)
public class VideoGraphQLControllerTest {

    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListVideosShouldReturn() {
        // given
        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";
        final var expectedRating = "";
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.of();
        final var expectedCategories = Set.of();
        final var expectedGenres = Set.of();

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedVideos.size(), expectedVideos));

        final var query = """
                {
                  videos {
                    id
                    title
                    description
                    yearLaunched
                    rating
                    duration
                    opened
                    published
                    video
                    trailer
                    banner
                    thumbnail
                    thumbnailHalf
                    castMembersId
                    castMembers
                    categoriesId
                    categories
                    genresId
                    genres
                    createdAt
                    updatedAt
                  }
                }
                """;

        // when
        final var res = this.graphql.document(query).execute();

        final var actualVideos = res.path("videos")
                .entityList(ListVideoUseCase.Output.class)
                .get();

        // then
        Assertions.assertTrue(
                actualVideos.size() == expectedVideos.size()
                        && actualVideos.containsAll(expectedVideos)
        );

        final var capturer = ArgumentCaptor.forClass(ListVideoUseCase.Input.class);

        verify(this.listVideoUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCastMembers, actualQuery.castMembers());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
        Assertions.assertEquals(expectedGenres, actualQuery.genres());
        Assertions.assertEquals(expectedYearLaunched, actualQuery.launchedAt());
        Assertions.assertEquals(expectedRating, actualQuery.rating());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListGenresShouldReturn() {
        // given
        final var expectedVideos = List.of(
                ListVideoUseCase.Output.from(Fixture.Videos.java21()),
                ListVideoUseCase.Output.from(Fixture.Videos.systemDesign())
        );

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedDirection = "desc";
        final var expectedSearch = "asd";
        final var expectedRating = "L";
        final int expectedYearLaunched = 2012;
        final var expectedCastMembers = Set.of("ct1");
        final var expectedCategories = Set.of("c1");
        final var expectedGenres = Set.of("g1");

        when(this.listVideoUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedGenres.size(), expectedVideos));

        final var query = """
                query AllVideos($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String, $rating: String, $yearLaunched: Int, $castMembers: [String], $categories: [String], $genres: [String]) {
                                
                  videos(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction, rating: $rating, yearLaunched: $yearLaunched, castMembers: $castMembers, categories: $categories, genres: $genres) {
                    id
                    title
                    description
                    yearLaunched
                    rating
                    duration
                    opened
                    published
                    video
                    trailer
                    banner
                    thumbnail
                    thumbnailHalf
                    castMembersId
                    castMembers
                    categoriesId
                    categories
                    genresId
                    genres
                    createdAt
                    updatedAt
                  }
                }
                """;

        // when
        final var res = this.graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .variable("rating", expectedRating)
                .variable("yearLaunched", expectedYearLaunched)
                .variable("castMembers", expectedCastMembers)
                .variable("categories", expectedCategories)
                .variable("genres", expectedGenres)
                .execute();

        final var actualVideos = res.path("videos")
                .entityList(ListVideoUseCase.Output.class)
                .get();

        // then
        Assertions.assertTrue(
                actualVideos.size() == expectedVideos.size()
                        && actualVideos.containsAll(expectedVideos)
        );

        final var capturer = ArgumentCaptor.forClass(ListVideoUseCase.Input.class);

        verify(this.listVideoUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCastMembers, actualQuery.castMembers());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
        Assertions.assertEquals(expectedGenres, actualQuery.genres());
        Assertions.assertEquals(expectedYearLaunched, actualQuery.launchedAt());
        Assertions.assertEquals(expectedRating, actualQuery.rating());
    }
}
