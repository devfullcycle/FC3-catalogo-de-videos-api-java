package com.fullcycle.catalogo;

import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

// TODO: Create a test to remember that is required to create this bean
public class IntegrationTestConfiguration {

    @Bean
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }

    @Bean
    public CastMemberRepository castMemberRepository() {
        return Mockito.mock(CastMemberRepository.class);
    }

    @Bean
    public GenreRepository genreRepository() {
        return Mockito.mock(GenreRepository.class);
    }

    @Bean
    public VideoRepository videoRepository() {
        return Mockito.mock(VideoRepository.class);
    }

    @Bean
    public WebGraphQlSecurityInterceptor webGraphQlSecurityInterceptor() {
        return new WebGraphQlSecurityInterceptor();
    }
}
