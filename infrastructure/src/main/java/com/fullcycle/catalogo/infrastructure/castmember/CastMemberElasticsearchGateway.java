package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    @Override
    public CastMember save(CastMember aMember) {
        return null;
    }

    @Override
    public void deleteById(String anId) {

    }

    @Override
    public Optional<CastMember> findById(String anId) {
        return Optional.empty();
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        return null;
    }
}
