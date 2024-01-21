package com.fullcycle.catalogo.infrastructure.configuration;

import com.fullcycle.catalogo.infrastructure.configuration.properties.RestClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "rest-client.categories")
    public RestClientProperties categoryRestClientProperties() {
        return new RestClientProperties();
    }

    @Bean
    public RestClient categoryHttpClient(final RestClientProperties properties) {
        final var factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .build();
    }
}
