package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.application.castmember.get.GetAllCastMembersByIdUseCase;
import com.fullcycle.catalogo.application.castmember.list.ListCastMembersOutput;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;

public final class GqlCastMemberPresenter {

    private GqlCastMemberPresenter() {}

    public static GqlCastMember present(final ListCastMembersOutput out) {
        return new GqlCastMember(out.id(), out.name(), out.type().name(), out.createdAt().toString(), out.updatedAt().toString());
    }

    public static GqlCastMember present(final GetAllCastMembersByIdUseCase.Output out) {
        return new GqlCastMember(out.id(), out.name(), out.type().name(), out.createdAt().toString(), out.updatedAt().toString());
    }

    public static GqlCastMember present(final CastMember out) {
        return new GqlCastMember(out.id(), out.name(), out.type().name(), out.createdAt().toString(), out.updatedAt().toString());
    }
}
