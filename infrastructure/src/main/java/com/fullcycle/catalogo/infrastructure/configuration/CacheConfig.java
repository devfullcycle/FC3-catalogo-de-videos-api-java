package com.fullcycle.catalogo.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.Cache2kBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig {

    @Bean
    Cache2kBuilderCustomizer cache2kBuilderCustomizer(
            @Value("${cache.max-entries}") final int maxEntries,
            @Value("${cache.ttl}") final int ttl
    ) {
        return builder -> builder
                .entryCapacity(maxEntries)
                .expireAfterWrite(ttl, TimeUnit.SECONDS);
    }
}
