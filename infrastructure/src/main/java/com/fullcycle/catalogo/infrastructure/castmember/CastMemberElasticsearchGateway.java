package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberDocument;
import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberElasticsearchGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember save(final CastMember aMember) {
        this.castMemberRepository.save(CastMemberDocument.from(aMember));
        return aMember;
    }

    @Override
    public void deleteById(final String anId) {
        this.castMemberRepository.deleteById(anId);
    }

    @Override
    public Optional<CastMember> findById(final String anId) {
        return this.castMemberRepository.findById(anId)
                .map(CastMemberDocument::toCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        return null;
    }
}
