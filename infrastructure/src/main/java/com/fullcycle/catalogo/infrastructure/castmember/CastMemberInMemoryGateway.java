package com.fullcycle.catalogo.infrastructure.castmember;

import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("development")
public class CastMemberInMemoryGateway implements CastMemberGateway {

    private final Map<String, CastMember> db;

    public CastMemberInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public CastMember save(final CastMember aCastMember) {
        this.db.put(aCastMember.id(), aCastMember);
        return aCastMember;
    }

    @Override
    public void deleteById(String genreId) {
        this.db.remove(genreId);
    }

    @Override
    public Optional<CastMember> findById(String genreId) {
        return Optional.ofNullable(this.db.get(genreId));
    }

    @Override
    public List<CastMember> findAllById(Set<String> genreId) {
        if (genreId == null || genreId.isEmpty()) {
            return List.of();
        }
        return genreId.stream()
                .map(this.db::get)
                .toList();
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                this.db.values().size(),
                this.db.values().stream().toList()
        );
    }
}
