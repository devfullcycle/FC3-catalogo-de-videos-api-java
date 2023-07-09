package com.fullcycle.catalogo.infrastructure.category.persistence;

import com.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryRepository extends ElasticsearchRepository<CategoryDocument, String> {
}
