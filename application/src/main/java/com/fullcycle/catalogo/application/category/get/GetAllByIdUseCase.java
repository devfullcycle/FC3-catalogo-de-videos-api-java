package com.fullcycle.catalogo.application.category.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GetAllByIdUseCase extends UseCase<GetAllByIdUseCase.Input, List<GetAllByIdUseCase.Output>> {

    private final CategoryGateway categoryGateway;

    public GetAllByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public List<Output> execute(final Input in) {
        if (in.ids().isEmpty()) {
            return List.of();
        }

        return this.categoryGateway.findAllById(in.ids()).stream()
                .map(Output::new)
                .toList();
    }

    public record Input(List<String> ids) {
        @Override
        public List<String> ids() {
            return ids != null ? ids : Collections.emptyList();
        }
    }

    public record Output(
            String id,
            String name,
            Instant createdAt,
            Instant updatedAt
    ) {

        public Output(final Category aCategory) {
            this(
                    aCategory.id(),
                    aCategory.name(),
                    aCategory.createdAt(),
                    aCategory.updatedAt()
            );
        }
    }
}
