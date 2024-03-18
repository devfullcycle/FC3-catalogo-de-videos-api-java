package com.fullcycle.catalogo.infrastructure.video;

import com.fullcycle.catalogo.AbstractElasticsearchTest;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.utils.IdUtils;
import com.fullcycle.catalogo.domain.utils.InstantUtils;
import com.fullcycle.catalogo.domain.video.Rating;
import com.fullcycle.catalogo.domain.video.Video;
import com.fullcycle.catalogo.domain.video.VideoSearchQuery;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import com.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

import java.time.Year;
import java.util.Set;

class VideoElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private VideoElasticsearchGateway videoGateway;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(this.videoRepository);
        Assertions.assertNotNull(this.videoGateway);
    }

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
        final var expectedOpened = true;
        final var expectedPublished = true;
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

        Assertions.assertEquals(0, this.videoRepository.count());

        // when
        final var input = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString(),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        final var actualOutput = this.videoGateway.save(input);

        // then
        Assertions.assertEquals(1, this.videoRepository.count());

        Assertions.assertEquals(input, actualOutput);

        var actualVideo = this.videoRepository.findById(expectedId).get();
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedCreatedAt.toString(), actualVideo.createdAt());
        Assertions.assertEquals(expectedUpdatedAt.toString(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
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
    public void givenMinimalInput_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedId = IdUtils.uniqueId();
        final var expectedTitle = "Java 21";
        final var expectedRating = Fixture.Videos.rating();
        final var expectedDuration = 2.0;
        final var expectedLaunchedAt = Year.of(2024);
        final var expectedDates = InstantUtils.now();
        final String expectedDescription = null;
        final Boolean expectedOpened = false;
        final Boolean expectedPublished = false;
        final String expectedVideo = null;
        final String expectedTrailer = null;
        final String expectedBanner = null;
        final String expectedThumbnail = null;
        final String expectedThumbnailHalf = null;
        final Set<String> expectedCastMembers = Set.of();
        final Set<String> expectedCategories = Set.of();
        final Set<String> expectedGenres = Set.of();

        Assertions.assertEquals(0, this.videoRepository.count());

        // when
        final var input = Video.with(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedDates.toString(),
                expectedDates.toString(),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCategories,
                expectedCastMembers,
                expectedGenres
        );

        final var actualOutput = this.videoGateway.save(input);

        // then
        Assertions.assertEquals(1, this.videoRepository.count());

        Assertions.assertEquals(input, actualOutput);

        var actualVideo = this.videoRepository.findById(expectedId).get();
        Assertions.assertNotNull(actualVideo);
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(expectedDates.toString(), actualVideo.createdAt());
        Assertions.assertEquals(expectedDates.toString(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchedAt.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating.getName(), actualVideo.rating());
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
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var java21 = Fixture.Videos.java21();

        this.videoRepository.save(VideoDocument.from(java21));

        final var expectedId = java21.id();
        Assertions.assertTrue(this.videoRepository.existsById(expectedId));

        // when
        this.videoGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(this.videoRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "any";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenNullId_whenCallsDeleteById_shouldBeOk() {
        // given
        final String expectedId = null;

        // when/then
        Assertions.assertDoesNotThrow(() -> this.videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenEmptyId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = " ";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.videoGateway.deleteById(expectedId));
    }

    @Test
    public void givenVideoPersisted_whenCallsFindById_shouldRetrieveIt() {
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
        final var expectedOpened = true;
        final var expectedPublished = true;
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

        Assertions.assertEquals(0, this.videoRepository.count());

        this.videoRepository.save(new VideoDocument(
                expectedId,
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt.getValue(),
                expectedDuration,
                expectedRating.getName(),
                expectedOpened,
                expectedPublished,
                expectedCreatedAt.toString(),
                expectedUpdatedAt.toString(),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumbnail,
                expectedThumbnailHalf,
                expectedCastMembers,
                expectedCategories,
                expectedGenres
        ));

        // when
        final var actualVideo = this.videoGateway.findById(expectedId).get();

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
    public void givenMismatchId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = IdUtils.uniqueId();

        Assertions.assertEquals(0, this.videoRepository.count());

        // when
        final var actualVideo = this.videoGateway.findById(expectedId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    @Test
    public void givenNullId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final String expectedId = null;

        Assertions.assertEquals(0, this.videoRepository.count());

        // when
        final var actualVideo = this.videoGateway.findById(expectedId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    @Test
    public void givenEmptyId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = " ";

        Assertions.assertEquals(0, this.videoRepository.count());

        // when
        final var actualVideo = this.videoGateway.findById(expectedId);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertTrue(actualVideo.isEmpty());
    }

    @Test
    public void givenEmptyVideos_whenCallsFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();

        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());
    }

    @ParameterizedTest
    @CsvSource({
            "go,0,10,1,1,Golang 1.22",
            "jav,0,10,1,1,Java 21",
            "design,0,10,1,1,System Design no Mercado Livre na prática",
            "assistido,0,10,1,1,System Design no Mercado Livre na prática",
            "FTW,0,10,1,1,Java 21",
            "linguagem,0,10,1,1,Golang 1.22",
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedTitle
    ) {
        // given
        mockVideos();

        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedTitle, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "meeting,0,10,1,1,Golang 1.22",
            "aulas,0,10,1,1,System Design no Mercado Livre na prática",
            "lives,0,10,1,1,Java 21",
            ",0,10,3,3,Golang 1.22",
    })
    public void givenValidCategory_whenCallsFindAll_shouldReturnElementsFiltered(
            final String categories,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCategories = categories == null ? Set.<String>of() : Set.of(categories);
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCastMembers = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "gabriel,0,10,1,1,Java 21",
            "wesley,0,10,1,1,Golang 1.22",
            "luiz,0,10,1,1,System Design no Mercado Livre na prática",
            ",0,10,3,3,Golang 1.22",
    })
    public void givenValidCastMember_whenCallsFindAll_shouldReturnElementsFiltered(
            final String castMember,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = castMember == null ? Set.<String>of() : Set.of(castMember);
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCategories = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "java,0,10,1,1,Java 21",
            "golang,0,10,1,1,Golang 1.22",
            "systemdesign,0,10,1,1,System Design no Mercado Livre na prática",
            ",0,10,3,3,Golang 1.22",
    })
    public void givenValidGenre_whenCallsFindAll_shouldReturnElementsFiltered(
            final String genre,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedGenres = genre == null ? Set.<String>of() : Set.of(genre);
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCategories = Set.<String>of();
        final var expectedCastMembers = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,3,3,Golang 1.22",
            "title,desc,0,10,3,3,System Design no Mercado Livre na prática",
            "created_at,asc,0,10,3,3,System Design no Mercado Livre na prática",
            "created_at,desc,0,10,3,3,Java 21",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnElementsSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCategories = Set.<String>of();
        final var expectedCastMembers = Set.<String>of();
        final var expectedGenres = Set.<String>of();


        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Golang 1.22",
            "1,1,1,3,Java 21",
            "2,1,1,3,System Design no Mercado Livre na prática",
            "3,1,0,3,",
    })
    public void givenValidPage_whenCallsFindAll_shouldReturnElementsPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final String expectedRating = null;
        final Integer expectedYearLaunched = null;
        final var expectedCategories = Set.<String>of();
        final var expectedCastMembers = Set.<String>of();
        final var expectedGenres = Set.<String>of();

        final var aQuery = new VideoSearchQuery(
                expectedPage, expectedPerPage, expectedTerms,
                expectedSort, expectedDirection, expectedRating, expectedYearLaunched,
                expectedCategories, expectedCastMembers, expectedGenres
        );

        // when
        final var actualOutput = this.videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());

        if (StringUtils.isNotEmpty(expectedName)) {
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).title());
        }
    }

    private void mockVideos() {
        this.videoRepository.save(VideoDocument.from(Fixture.Videos.systemDesign()));
        this.videoRepository.save(VideoDocument.from(Fixture.Videos.golang()));
        this.videoRepository.save(VideoDocument.from(Fixture.Videos.java21()));
    }
}