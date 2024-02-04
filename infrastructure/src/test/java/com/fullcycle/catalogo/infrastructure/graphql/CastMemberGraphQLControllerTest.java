package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.GraphQLControllerTest;
import com.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.list.ListCastMembersOutput;
import com.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = CastMemberGraphQLController.class)
public class CastMemberGraphQLControllerTest {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListCastMembersShouldReturn() {
        // given
        final var expectedMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedSearch = "";

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedMembers.size(), expectedMembers));

        final var query = """
                {
                  castMembers {
                    id
                    name
                    type
                    createdAt
                    updatedAt
                  }
                }
                """;

        // when
        final var res = this.graphql.document(query).execute();

        final var actualCategories = res.path("castMembers")
                .entityList(ListCastMembersOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                actualCategories.size() == expectedMembers.size()
                        && actualCategories.containsAll(expectedMembers)
        );

        final var capturer = ArgumentCaptor.forClass(CastMemberSearchQuery.class);

        verify(this.listCastMemberUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCastMembersShouldReturn() {
        // given
        final var expectedMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedDirection = "desc";
        final var expectedSearch = "asd";

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedMembers.size(), expectedMembers));

        final var query = """
                query AllCastMembers($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String) {
                                
                  castMembers(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction) {
                    id
                    name
                    type
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
                .execute();

        final var actualCategories = res.path("castMembers")
                .entityList(ListCastMembersOutput.class)
                .get();

        // then
        Assertions.assertTrue(
                actualCategories.size() == expectedMembers.size()
                        && actualCategories.containsAll(expectedMembers)
        );

        final var capturer = ArgumentCaptor.forClass(CastMemberSearchQuery.class);

        verify(this.listCastMemberUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
    }

    @Test
    public void givenCastMemberInputWhenCallsSaveCastMemberMutationShouldPersistAndReturn() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "Gabriel";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "type", expectedType.name(),
                "createdAt", expectedCreatedAt.toString(),
                "updatedAt", expectedUpdatedAt.toString()
        );

        final var query = """
                mutation SaveCastMember($input: CastMemberInput!) {
                    castMember: saveCastMember(input: $input) {
                        id
                        name
                        type
                        createdAt
                        updatedAt
                    }
                }
                """;

        doAnswer(returnsFirstArg()).when(saveCastMemberUseCase).execute(any());

        // when
        this.graphql.document(query)
                .variable("input", input)
                .execute()
                .path("castMember.id").entity(String.class).isEqualTo(expectedId)
                .path("castMember.name").entity(String.class).isEqualTo(expectedName)
                .path("castMember.type").entity(CastMemberType.class).isEqualTo(expectedType)
                .path("castMember.createdAt").entity(Instant.class).isEqualTo(expectedCreatedAt)
                .path("castMember.updatedAt").entity(Instant.class).isEqualTo(expectedUpdatedAt);

        // then
        final var capturer = ArgumentCaptor.forClass(CastMember.class);

        verify(this.saveCastMemberUseCase, times(1)).execute(capturer.capture());

        final var actualCategory = capturer.getValue();
        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedType, actualCategory.type());
        Assertions.assertEquals(expectedCreatedAt, actualCategory.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualCategory.updatedAt());
    }
}
