package com.fullcycle.catalogo;

import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class IntegrationTestConfiguration {

    @Bean
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }
}
