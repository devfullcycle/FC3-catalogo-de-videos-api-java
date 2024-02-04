package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CastMemberInMemoryGateway implements CastMemberGateway {

    private final Map<String, CastMember> db;

    public CastMemberInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public CastMember save(CastMember aMember) {
        this.db.put(aMember.id(), aMember);
        return aMember;
    }

    @Override
    public void deleteById(String anId) {
        this.db.remove(anId);
    }

    @Override
    public Optional<CastMember> findById(String anId) {
        return Optional.ofNullable(this.db.get(anId));
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.size(),
                this.db.values().stream().toList()
        );
    }
}
