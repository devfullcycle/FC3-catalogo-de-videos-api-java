package com.fullcycle.catalogo.domain.video;

import com.fullcycle.catalogo.domain.UnitTest;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsVideoWith_shouldInstantiate() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenValidVideo_whenCallsVideoWith_shouldInstantiate() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        final var video = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // when
        final var actualVideo = Video.with(video);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenNullCollections_whenCallsVideoWith_shouldAssignEmptySet() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.<String>of();
        final var expectedCastMembers = Set.<String>of();
        final var expectedGenres = Set.<String>of();
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                null,
                null,
                null
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenEmptyVideo_whenCallsVideoWith_shouldOverwritePublishedToFalse() {
        // given
        final var actualPublished = true;

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = " ";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                actualPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenEmptyTrailer_whenCallsVideoWith_shouldOverwritePublishedToFalse() {
        // given
        final var actualPublished = true;

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = " ";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                actualPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenEmptyBanner_whenCallsVideoWith_shouldOverwritePublishedToFalse() {
        // given
        final var actualPublished = true;

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                actualPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenEmptyThumbnail_whenCallsVideoWith_shouldOverwritePublishedToFalse() {
        // given
        final var actualPublished = true;

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                actualPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenEmptyThumbnailHalf_whenCallsVideoWith_shouldOverwritePublishedToFalse() {
        // given
        final var actualPublished = true;

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "";

        // when
        final var actualVideo = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedRating,
                expectedOpened,
                actualPublished,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt, actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(expectedCategories, actualVideo.categories());
        Assertions.assertEquals(expectedGenres, actualVideo.genres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbnailHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenAnInvalidNullId_whenCallWith_thenShouldReceiveError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final String expectedId = null;
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Video.with(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedVideo,
                        expectedTrailer,
                        expectedBanner,
                        expectedThumbnail,
                        expectedThumbnailHalf,
                        expectedCategories,
                        expectedCastMembers,
                        expectedGenres
                )
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullTitle_whenCallWith_thenShouldReceiveError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var expectedId = IdUtils.uniqueId();
        final String expectedTitle = null;
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Video.with(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedVideo,
                        expectedTrailer,
                        expectedBanner,
                        expectedThumbnail,
                        expectedThumbnailHalf,
                        expectedCategories,
                        expectedCastMembers,
                        expectedGenres
                )
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullLaunchedAt_whenCallWith_thenShouldReceiveError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be empty";

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System DEsign";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Video.with(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedVideo,
                        expectedTrailer,
                        expectedBanner,
                        expectedThumbnail,
                        expectedThumbnailHalf,
                        expectedCategories,
                        expectedCastMembers,
                        expectedGenres
                )
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullRating_whenCallWith_thenShouldReceiveError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be empty";

        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "System DEsign";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.now();
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedCategories = Set.of(IdUtils.uniqueId());
        final var expectedCastMembers = Set.of(IdUtils.uniqueId());
        final var expectedGenres = Set.of(IdUtils.uniqueId());
        final var expectedVideo = "http://video";
        final var expectedTrailer = "http://trailer";
        final var expectedBanner = "http://banner";
        final var expectedThumbnail = "http://thumb";
        final var expectedThumbnailHalf = "http://thumbhalf";

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> Video.with(
                        expectedId,
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchedAt,
                        expectedDuration,
                        expectedRating,
                        expectedOpened,
                        expectedPublished,
                        expectedCreatedAt,
                        expectedUpdatedAt,
                        expectedVideo,
                        expectedTrailer,
                        expectedBanner,
                        expectedThumbnail,
                        expectedThumbnailHalf,
                        expectedCategories,
                        expectedCastMembers,
                        expectedGenres
                )
        );

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
