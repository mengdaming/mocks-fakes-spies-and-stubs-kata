package kata.domain.rate;

import kata.domain.film.Film;

import static kata.domain.film.FilmDummy.randomFilm;

import kata.domain.film.FilmService;

import static kata.domain.rate.RateDummy.*;

import kata.domain.user.UserId;
import kata.domain.user.UserIdDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class RateServiceTest_Mock_Stub_Spy {
    private RateRepository repository;
    private FilmService filmService;
    private RateService rateService;
    private LikedNotifier likedNotifier;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(RateRepository.class);
        filmService = Mockito.mock(FilmService.class);
        likedNotifier = Mockito.mock(LikedNotifier.class);
        rateService = new RateService(repository, filmService, likedNotifier);
    }

    @Test
    void shouldSaveInTheRepository_usingASpy_withMockito() {
        final Rate rate = Rate.of("aTitle", 4, UserIdDummy.randomUserId());

        // Exercise
        rateService.save(rate);

        // Verify expectations
    }

    @Test
    void shouldReceiveFromRepository_usingAStub_withMockito() {
        final Rate rate = Rate.of("aTitle", 4, UserIdDummy.randomUserId());

        // Setup

        // Exercise
        final Optional<Rate> ratingFromRepo = rateService.findById(rate.id);

        // Verify State
    }

    @Test
    void shouldReturnRatesMadeByAUser_usingAStub_withMockito() {
        final UserId userId = UserId.of("aUser");
        final Rate rateOneByUser = randomRate().withUserId(userId).build();
        final Rate rateTwoByUser = randomRate().withUserId(userId).build();

        final List<Rate> allRates = randomListOfRatesOfSize(10);
        allRates.add(rateOneByUser);
        allRates.add(rateTwoByUser);

        // Setup

        // Exercise
        final List<Rate> ratedByUser = rateService.findByUser(userId);

        // Verify State
    }

    @Test
    void shouldReturnTheListOfRatesByAUserForAFilmThatWasProducedAtYearOrMoreRecent_usingAStub_withMockito() {
        final UserId userId = UserId.of("aUser");
        final int productionYear = 2000;

        final String theLionKingTitle = "The Lion King";
        final Film theLionKingMovieAsOldFilm = randomFilm()
                .withTitle(theLionKingTitle)
                .withReleaseDate(1994)
                .build();
        final String frozenTitle = "Frozen";
        final Film frozenMovieAsNewerFilm = randomFilm()
                .withTitle(frozenTitle)
                .withReleaseDate(2013)
                .build();
        final Rate rateOfFrozenByUser = randomRate()
                .withTitle(frozenTitle)
                .withUserId(userId)
                .build();
        final Rate rateOfTheLionKingByUser = randomRate()
                .withTitle(theLionKingTitle)
                .withUserId(userId)
                .build();
        final List<Rate> allRates = randomListOfRatesOfSize(10);
        allRates.add(rateOfFrozenByUser);
        allRates.add(rateOfTheLionKingByUser);

        // Setup

        // Exercise
        final List<Rate> ratesByUserOfFilmsMadeAtYear2000OrMoreRecent = rateService
                .ratedByUserAtYearOrMoreRecent(userId, productionYear);

        // Verify State
    }

    @Test
    void whenAFilmIsRatedMoreThan10Times_ItShouldNotifyOnceThatItHasBeenLikedBy10DifferentUsers_usingASpy_withMockito() {
        final String title = "aTitle";
        final List<Rate> ratesForFilm = randomListOfRatesOfSizeForFilm(RateService.RATES_PER_FILM_FOR_NOTIFICATION, title);

        // Setup

        // Exercise
        rateService.save(randomRate().withTitle(title).build());

        // Verify it has been called
    }

    @Test
    void whenAFilmIsRatedMoreThan10Times_ItShouldNotifyOnceThatItHasBeenLikedBy10DifferentUsers_usingAMock_withoutMockito() {

        // IMPORTANT: This is a Mockito-free zone!
        // You are expected to write your own mock for this test case

        final String title = "aTitle";
        final Rate rate = randomRate().withTitle(title).build();
        final List<Rate> ratesForFilm = randomListOfRatesOfSizeForFilm(RateService.RATES_PER_FILM_FOR_NOTIFICATION, title);


        // Setup Expectations

        // Exercise
        rateService.save(rate);

        // Verify expectations
    }
}
