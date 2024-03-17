package com.fullcycle.catalogo.application.castmember.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllByIdUseCase extends UseCase<GetAllByIdUseCase.Input, List<GetAllByIdUseCase.Output>> {

    private final CastMemberGateway castMemberGateway;

    public GetAllByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public List<Output> execute(final Input in) {
        if (in.ids().isEmpty()) {
            return List.of();
        }

        return this.castMemberGateway.findAllById(in.ids()).stream()
                .map(Output::new)
                .toList();
    }

    public record Input(Set<String> ids) {
        @Override
        public Set<String> ids() {
            return ids != null ? ids : Collections.emptySet();
        }
    }

    public record Output(
            String id,
            String name,
            CastMemberType type,
            Instant createdAt,
            Instant updatedAt
    ) {

        public Output(final CastMember aMember) {
            this(
                    aMember.id(),
                    aMember.name(),
                    aMember.type(),
                    aMember.createdAt(),
                    aMember.updatedAt()
            );
        }
    }
}
