package com.fullcycle.catalogo.application.castmember.list;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ListCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidQuery_whenCallsListCastMembers_shouldReturnIt() {
        // given
        final var members = List.of(
                Fixture.CastMembers.gabriel(),
                Fixture.CastMembers.wesley()
        );

        final var expectedItems = members.stream()
                .map(ListCastMembersOutput::from)
                .toList();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var pagination =
                new Pagination<>(expectedPage, expectedPerPage, members.size(), members);

        when(this.castMemberGateway.findAll(any()))
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
