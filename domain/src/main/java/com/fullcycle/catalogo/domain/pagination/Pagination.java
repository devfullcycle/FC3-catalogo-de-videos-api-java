package com.fullcycle.catalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        Metadata meta,
        List<T> data
) {

    public Pagination(
            int currentPage,
            int perPage,
            long total,
            List<T> data
    ) {
        this(new Metadata(currentPage, perPage, total), data);
    }

    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> aNewList = this.data.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(meta(), aNewList);
    }
}
