package com.fullcycle.catalogo;

import com.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
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
    public WebGraphQlSecurityInterceptor webGraphQlSecurityInterceptor() {
        return new WebGraphQlSecurityInterceptor();
    }

    public static class WebGraphQlSecurityInterceptor implements WebGraphQlInterceptor {

        private List<SimpleGrantedAuthority> authorities;

        public WebGraphQlSecurityInterceptor() {
            this.authorities = new ArrayList<>();
        }

        public void setAuthorities(final String... roles) {
            if (roles == null) {
                this.authorities = new ArrayList<>();
            } else {
                this.authorities = Arrays.stream(roles).map(SimpleGrantedAuthority::new).toList();
            }
        }

        @Override
        public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
            if (authorities.isEmpty()) {
                return chain.next(request);
            }

            final var authenticated = UsernamePasswordAuthenticationToken.authenticated("JohnDoe", "123456", authorities);
            final var context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticated);
            return chain.next(request)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
        }
    }
}
