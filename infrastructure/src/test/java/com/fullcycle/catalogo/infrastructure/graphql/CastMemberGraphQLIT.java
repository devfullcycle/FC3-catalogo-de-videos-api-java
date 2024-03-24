package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.IntegrationTest;
import com.fullcycle.catalogo.WebGraphQlSecurityInterceptor;
import com.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.list.ListCastMembersOutput;
import com.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.configuration.security.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.test.tester.WebGraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@IntegrationTest
public class CastMemberGraphQLIT {

    @MockBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private WebGraphQlHandler webGraphQlHandler;

    @Autowired
    private WebGraphQlSecurityInterceptor interceptor;

    @Test
    public void givenAnonymousUser_whenQueries_shouldReturnUnauthorized() {
        interceptor.setAuthorities();
        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().expect(err -> "Unauthorized".equals(err.getMessage()) && "castMembers".equals(err.getPath()))
                .verify();
    }

    @Test
    public void givenUserWithAdminRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_ADMIN);

        final var castMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedIds = castMembers.stream().map(ListCastMembersOutput::id).toList();

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, castMembers.size(), castMembers));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithSubscriberRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_SUBSCRIBER);

        final var castMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedIds = castMembers.stream().map(ListCastMembersOutput::id).toList();

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, castMembers.size(), castMembers));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);
    }

    @Test
    public void givenUserWithCastMembersRole_whenQueries_shouldReturnResult() {
        interceptor.setAuthorities(Roles.ROLE_CAST_MEMBERS);

        final var castMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedIds = castMembers.stream().map(ListCastMembersOutput::id).toList();

        when(this.listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(0, 10, castMembers.size(), castMembers));

        final var document = "query castMembers { castMembers { id } }";
        final var graphQlTesters = WebGraphQlTester.create(webGraphQlHandler);
        graphQlTesters.document(document).execute()
                .errors().verify()
                .path("castMembers[*].id").entityList(String.class).isEqualTo(expectedIds);
    }
}
