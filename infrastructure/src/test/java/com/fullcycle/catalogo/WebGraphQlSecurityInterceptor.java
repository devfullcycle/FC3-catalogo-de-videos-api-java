package com.fullcycle.catalogo;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebGraphQlSecurityInterceptor implements WebGraphQlInterceptor {

    private final List<SimpleGrantedAuthority> authorities;

    public WebGraphQlSecurityInterceptor() {
        this.authorities = new ArrayList<>();
    }

    public void setAuthorities(final String... authorities) {
        this.authorities.clear();
        if (authorities != null) {
            this.authorities.addAll(Arrays.stream(authorities).map(SimpleGrantedAuthority::new).toList());
        }
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        if (this.authorities.isEmpty()) {
            return chain.next(request);
        }

        final var user = UsernamePasswordAuthenticationToken.authenticated("JohnDoe", "123456", authorities);
        final var context = SecurityContextHolder.getContext();
        context.setAuthentication(user);

        return chain.next(request)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
    }
}
