package com.fullcycle.catalogo.domain.castmember;

import com.fullcycle.catalogo.domain.UnitTest;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import com.fullcycle.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateACastMember() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Gabriel";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        // when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        // then
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedID, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDates, actualMember.createdAt());
        Assertions.assertEquals(expectedDates, actualMember.updatedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithCastMember_thenInstantiateACastMember() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Filmes";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        final var aMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        // then
        final var actualMember = CastMember.with(aMember);

        // when
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(aMember.id(), actualMember.id());
        Assertions.assertEquals(aMember.name(), actualMember.name());
        Assertions.assertEquals(aMember.type(), actualMember.type());
        Assertions.assertEquals(aMember.createdAt(), actualMember.createdAt());
        Assertions.assertEquals(aMember.updatedAt(), actualMember.updatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final String expectedName = null;
        final var expectedID = UUID.randomUUID().toString();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final String expectedName = " ";
        final var expectedID = UUID.randomUUID().toString();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final String expectedID = null;
        final var expectedName = "Aulas";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final var expectedID = " ";
        final var expectedName = "Aulas";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullType_whenCallWithAndValidate_thenShouldReceiveError() {
        // given
        final var expectedID = UUID.randomUUID().toString();
        final var expectedName = "Aulas";
        final CastMemberType expectedType = null;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        // when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
