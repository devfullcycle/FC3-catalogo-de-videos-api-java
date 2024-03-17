package com.fullcycle.catalogo.domain.castmember;

import com.fullcycle.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember save(CastMember aMember);

    void deleteById(String anId);

    Optional<CastMember> findById(String anId);

    List<CastMember> findAllById(List<String> ids);

    Pagination<CastMember> findAll(CastMemberSearchQuery aQuery);
}
