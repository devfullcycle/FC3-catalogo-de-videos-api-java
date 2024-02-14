package com.fullcycle.catalogo.application.video.save;

import com.fullcycle.catalogo.application.UseCaseTest;
import com.fullcycle.catalogo.domain.exceptions.DomainException;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import com.fullcycle.catalogo.domain.video.Rating;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SaveVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Test
    public void givenValidInput_whenCallsSave_shouldPersistIt() {
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

        when(videoGateway.save(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var input = new SaveVideoUseCase.Input(expectedId,
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

        final var actualOutput = this.useCase.execute(input);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1)).save(captor.capture());

        var actualVideo = captor.getValue();
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
    public void givenNullInput_whenCallsSave_shouldReturnError() {
        // given
        final SaveVideoUseCase.Input input = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'SaveVideoUseCase.Input' cannot be null";

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(input));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(videoGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given
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

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        // when
        final var input = new SaveVideoUseCase.Input(expectedId,
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

        final var actualError = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(input)
        );

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(videoGateway, times(0)).save(any());
    }
}
