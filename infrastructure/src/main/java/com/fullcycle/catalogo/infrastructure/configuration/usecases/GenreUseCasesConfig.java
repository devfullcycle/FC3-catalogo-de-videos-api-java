package com.fullcycle.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.catalogo.application.genre.get.GetAllByIdUseCase;
import com.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import com.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import com.fullcycle.catalogo.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class GenreUseCasesConfig {

    private final GenreGateway genreGateway;

    public GenreUseCasesConfig(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    SaveGenreUseCase saveGenreUseCase() {
        return new SaveGenreUseCase(genreGateway);
    }

    @Bean
    DeleteGenreUseCase deleteGenreUseCase() {
        return new DeleteGenreUseCase(genreGateway);
    }

    @Bean
    ListGenreUseCase listGenreUseCase() {
        return new ListGenreUseCase(genreGateway);
    }

    @Bean
    GetAllByIdUseCase getAllByIdUseCase() {
        return new GetAllByIdUseCase(genreGateway);
    }
}
