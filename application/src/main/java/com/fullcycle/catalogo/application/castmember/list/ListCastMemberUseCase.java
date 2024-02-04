package com.fullcycle.catalogo.application.castmember.list;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class ListCastMemberUseCase extends UseCase<CastMemberSearchQuery, Pagination<ListCastMembersOutput>> {

    private final CastMemberGateway castMemberGateway;

    public ListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<ListCastMembersOutput> execute(final CastMemberSearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery)
                .map(ListCastMembersOutput::from);
    }
}
