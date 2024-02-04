package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
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
}