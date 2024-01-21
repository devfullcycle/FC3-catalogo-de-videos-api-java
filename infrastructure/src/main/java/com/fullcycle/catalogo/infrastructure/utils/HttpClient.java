package com.fullcycle.catalogo.infrastructure.utils;

import com.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface HttpClient {

    Predicate<HttpStatusCode> isNotFound = HttpStatus.NOT_FOUND::equals;

    Predicate<HttpStatusCode> is5xx = HttpStatusCode::is5xxServerError;

    String namespace();

    default ErrorHandler notFoundHandler(final String id) {
        return (req, res) -> {
            throw NotFoundException.with("Not found observed from %s [resourceId:%s]".formatted(namespace(), id));
        };
    }

    default ErrorHandler a5xxHandler(final String id) {
        return (req, res) -> {
            throw InternalErrorException.with("Error observed from %s [resourceId:%s] [status:%s]".formatted(namespace(), id, res.getStatusCode().value()));
        };
    }

    default <T> Optional<T> doGet(final String id, final Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            final var cause = ExceptionUtils.getRootCause(ex);
            if (cause instanceof HttpConnectTimeoutException) {
                throw InternalErrorException.with("ConnectTimeout observed from %s [resourceId:%s]".formatted(namespace(), id), ex);
            }

            if (cause instanceof HttpTimeoutException || cause instanceof TimeoutException) {
                throw InternalErrorException.with("Timeout observed from %s [resourceId:%s]".formatted(namespace(), id), ex);
            }

            throw ex;
        }
    }
}
