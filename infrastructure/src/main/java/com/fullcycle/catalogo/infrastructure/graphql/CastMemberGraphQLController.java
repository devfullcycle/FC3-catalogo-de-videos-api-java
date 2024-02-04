package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import com.fullcycle.catalogo.application.castmember.list.ListCastMembersOutput;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CastMemberGraphQLController {

    private final ListCastMemberUseCase listCastMemberUseCase;

    public CastMemberGraphQLController(final ListCastMemberUseCase listCastMemberUseCase) {
        this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
    }

    @QueryMapping
    public List<ListCastMembersOutput> castMembers(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ) {
        final var query =
                new CastMemberSearchQuery(page, perPage, search, sort, direction);
        
        return this.listCastMemberUseCase.execute(query).data();
    }
}
