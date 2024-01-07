package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NoOpCategoryGateway implements CategoryGateway {

    @Override
    public Optional<Category> categoryOfId(final String anId) {
        return Optional.of(Category.with(
                anId,
                "Lives",
                null,
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        ));
    }
}
