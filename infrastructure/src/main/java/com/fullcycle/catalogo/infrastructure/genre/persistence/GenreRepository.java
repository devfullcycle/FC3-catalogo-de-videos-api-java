package com.fullcycle.catalogo.infrastructure.genre.persistence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GenreRepository extends ElasticsearchRepository<GenreDocument, String> {
}
