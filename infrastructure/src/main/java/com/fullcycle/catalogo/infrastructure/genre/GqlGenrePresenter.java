package com.fullcycle.catalogo.infrastructure.genre;

import com.fullcycle.catalogo.application.genre.get.GetAllGenresByIdUseCase;
import com.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import com.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;

import java.time.Instant;

public final class GqlGenrePresenter {

    private GqlGenrePresenter() {}

    public static GqlGenre present(final ListGenreUseCase.Output out) {
        return new GqlGenre(out.id(), out.name(), out.active(), out.categories(), formatDate(out.createdAt()), formatDate(out.updatedAt()), formatDate(out.deletedAt()));
    }

    public static GqlGenre present(final GetAllGenresByIdUseCase.Output out) {
        return new GqlGenre(out.id(), out.name(), out.active(), out.categories(), formatDate(out.createdAt()), formatDate(out.updatedAt()), formatDate(out.deletedAt()));
    }

    private static String formatDate(final Instant date) {
        return date != null ? date.toString() : "";
    }
}
