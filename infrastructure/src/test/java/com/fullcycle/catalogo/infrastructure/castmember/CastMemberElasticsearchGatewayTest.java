package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberDocument;
import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CastMemberElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CastMemberElasticsearchGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(this.castMemberRepository);
        Assertions.assertNotNull(this.castMemberGateway);
    }

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        // when
        final var actualOutput = this.castMemberGateway.save(gabriel);

        // then
        Assertions.assertEquals(gabriel, actualOutput);

        final var actualMember = this.castMemberRepository.findById(gabriel.id()).get();
        Assertions.assertEquals(gabriel.id(), actualMember.id());
        Assertions.assertEquals(gabriel.name(), actualMember.name());
        Assertions.assertEquals(gabriel.type(), actualMember.type());
        Assertions.assertEquals(gabriel.createdAt(), actualMember.createdAt());
        Assertions.assertEquals(gabriel.updatedAt(), actualMember.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        this.castMemberRepository.save(CastMemberDocument.from(gabriel));

        final var expectedId = gabriel.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        // when
        this.castMemberGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "any";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.castMemberGateway.deleteById(expectedId));
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var wesley = Fixture.CastMembers.wesley();

        this.castMemberRepository.save(CastMemberDocument.from(wesley));

        final var expectedId = wesley.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        // when
        final var actualOutput = this.castMemberGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(wesley.id(), actualOutput.id());
        Assertions.assertEquals(wesley.name(), actualOutput.name());
        Assertions.assertEquals(wesley.type(), actualOutput.type());
        Assertions.assertEquals(wesley.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(wesley.updatedAt(), actualOutput.updatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = "any";

        // when
        final var actualOutput = this.castMemberGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }
}