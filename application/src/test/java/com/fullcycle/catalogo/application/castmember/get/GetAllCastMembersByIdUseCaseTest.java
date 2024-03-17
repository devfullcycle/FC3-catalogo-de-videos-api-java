package com.fullcycle.catalogo.application.castmember.get;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllCastMembersByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetAllCastMembersByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidIds_whenCallsGetAllById_shouldReturnIt() {
        // given
        final var members = List.of(
                Fixture.CastMembers.gabriel(),
                Fixture.CastMembers.wesley()
        );

        final var expectedItems = members.stream()
                .map(GetAllCastMembersByIdUseCase.Output::new)
                .toList();

        final var expectedIds = members.stream().map(CastMember::id).collect(Collectors.toSet());

        when(this.castMemberGateway.findAllById(any()))
                .thenReturn(members);

        // when
        final var actualOutput = this.useCase.execute(new GetAllCastMembersByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.size() &&
                        expectedItems.containsAll(actualOutput)
        );

        verify(this.castMemberGateway, times(1)).findAllById(expectedIds);
    }

    @Test
    public void givenNullIds_whenCallsGetAllById_shouldReturnEmpty() {
        // given
        final Set<String> expectedIds = null;

        // when
        final var actualOutput = this.useCase.execute(new GetAllCastMembersByIdUseCase.Input(expectedIds));

        // then
        Assertions.assertTrue(actualOutput.isEmpty());

        verify(this.castMemberGateway, times(0)).findAllById(any());
    }
}