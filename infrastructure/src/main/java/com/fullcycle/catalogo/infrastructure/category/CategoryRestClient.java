package com.fullcycle.catalogo.infrastructure.category;

import com.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.infrastructure.category.models.CategoryDTO;
import com.fullcycle.catalogo.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class CategoryRestClient {

    private final RestClient restClient;

    public CategoryRestClient(final RestClient categoryHttpClient) {
        this.restClient = categoryHttpClient;
    }

    public Optional<CategoryDTO> getById(final String categoryId) {
        try {
            final CategoryDTO response = this.restClient.get()
                    .uri("/{id}", categoryId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, (req, res) -> {
                        throw NotFoundException.with("A category of ID %s was not found".formatted(categoryId));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw InternalErrorException.with("Failed to get Category of id %s".formatted(categoryId));
                    })
                    .body(CategoryDTO.class);

            return Optional.of(response);
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            if (ExceptionUtils.getRootCause(ex) instanceof TimeoutException) {
                throw InternalErrorException.with("Timeout from category of ID %s".formatted(categoryId), ex);
            }
            throw ex;
        }
    }
}
