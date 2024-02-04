package com.fullcycle.catalogo.application.castmember.save;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SaveCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt() {
        // given
        final var aMember = Fixture.CastMembers.gabriel();

        when(castMemberGateway.save(any()))
                .thenAnswer(returnsFirstArg());

        // when
        this.useCase.execute(aMember);

        // then
        verify(castMemberGateway, times(1)).save(eq(aMember));
    }

    @Test
    public void givenNullCastMember_whenCallsSave_shouldReturnError() {
        // given
        final CastMember aMember = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'aMember' cannot be null";

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aMember));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).save(eq(aMember));
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var aMember = CastMember.with(
                "",
                "Gabriel",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aMember));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).save(eq(aMember));
    }

    @Test
    public void givenInvalidName_whenCallsSave_shouldReturnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aMember = CastMember.with(
                "1231321",
                "",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aMember));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).save(eq(aMember));
    }
}
