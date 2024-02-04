package com.fullcycle.catalogo.infrastructure.castmember.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CastMemberEventTest {

    @Test
    public void givenCastMemberEvent_whenCallToCastMember_shouldConvertTimestampToInstantCorrectly() {
        // given
        final var expectedId = "123";
        final var expectedName = "Gabriel";
        final var expectedType = "ACTOR";
        final var expectedUnixTimestamp = 1707086611086071L;
        final var expectedDate = LocalDateTime.of(2024, 02, 04, 22, 43, 31)
                .toInstant(ZoneOffset.UTC);

        final var event = new CastMemberEvent(expectedId, expectedName, expectedType, expectedUnixTimestamp, expectedUnixTimestamp);

        // when
        final var actualMember = event.toCastMember();

        // then
        assertEquals(expectedId, actualMember.id());
        assertEquals(expectedName, actualMember.name());
        assertEquals(expectedType, actualMember.type().name());
        assertEquals(expectedDate, actualMember.createdAt().truncatedTo(ChronoUnit.SECONDS));
        assertEquals(expectedDate, actualMember.updatedAt().truncatedTo(ChronoUnit.SECONDS));
    }
}