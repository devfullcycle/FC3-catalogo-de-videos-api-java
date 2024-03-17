package com.fullcycle.catalogo.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.catalogo.application.video.get.GetVideoUseCase;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class VideoUseCasesConfig {

    private final VideoGateway videoGateway;

    public VideoUseCasesConfig(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    DeleteVideoUseCase deleteVideoUseCase() {
        return new DeleteVideoUseCase(videoGateway);
    }

    @Bean
    ListVideoUseCase listVideoUseCase() {
        return new ListVideoUseCase(videoGateway);
    }

    @Bean
    SaveVideoUseCase saveVideoUseCase() {
        return new SaveVideoUseCase(videoGateway);
    }

    @Bean
    GetVideoUseCase getVideoUseCase() {
        return new GetVideoUseCase(videoGateway);
    }
}
