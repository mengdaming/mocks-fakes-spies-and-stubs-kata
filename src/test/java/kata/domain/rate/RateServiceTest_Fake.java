package kata.domain.rate;

import kata.domain.film.Film;
import kata.domain.film.FilmService;
import kata.domain.user.UserId;
import kata.domain.user.UserIdDummy;
import kata.support.FilmRepositoryInMemory;
import kata.support.RateRepositoryInMemory;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static kata.domain.film.FilmDummy.randomFilm;
import static kata.domain.rate.RateDummy.*;

class RateServiceTest_Fake {
    private RateRepository repository;
    private FilmService filmService;
    private RateService rateService;
    private LikedNotifier likedNotifier;

    @BeforeEach
    void setup() {
        repository = new RateRepositoryInMemory();
//        filmService = Mockito.mock(FilmService.class);
        filmService = new FilmService(new FilmRepositoryInMemory());
        likedNotifier = Mockito.mock(LikedNotifier.class);
        rateService = new RateService(repository, filmService, likedNotifier);
    }

    @Test
    void shouldSaveInTheRepository() {
        final Rate rate = Rate.of("aTitle", 4, UserIdDummy.randomUserId());

        // Exercise
        rateService.save(rate);

        // Verify State
        Optional<Rate> savedRate = repository.findById(rate.id);
        assertEquals(rate, savedRate.get());
    }

    @Test
    void shouldReceiveFromRepository() {
        final Rate rate = Rate.of("aTitle", 4, UserIdDummy.randomUserId());
        repository.save(rate.id, rate);

        // Exercise
        final Optional<Rate> ratingFromRepo = rateService.findById(rate.id);

        // Verify State
        assertEquals(rate, ratingFromRepo.get());
    }

    @Test
    void shouldReturnRatesMadeByAUser() {
        final UserId userId = UserId.of("aUser");
        final Rate rateOneByUser = randomRate().withUserId(userId).build();
        final Rate rateTwoByUser = randomRate().withUserId(userId).build();

        final List<Rate> allRates = randomListOfRatesOfSize(10);
        allRates.add(rateOneByUser);
        allRates.add(rateTwoByUser);

        // Setup state
        for (Rate rate : allRates) {
            repository.save(rate.id, rate);
        }

        // Exercise
        final List<Rate> ratedByUser = rateService.findByUser(userId);

        // Verify State
        assertEquals(2, ratedByUser.size());
        assertTrue(ratedByUser.contains(rateOneByUser));
        assertTrue(ratedByUser.contains(rateTwoByUser));
    }

    @Test
    void shouldReturnTheListOfRatesByAUserForAFilmThatWasProducedAtYearOrMoreRecent() {
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

        // Setup expectations
        for (Rate rate : allRates) {
            repository.save(rate.id, rate);
        }
        filmService.save(theLionKingMovieAsOldFilm);
        filmService.save(frozenMovieAsNewerFilm);
//        Mockito.doReturn(Optional.of(frozenMovieAsNewerFilm)).when(filmService).findById(frozenTitle);

        // Exercise
        final List<Rate> ratesByUserOfFilmsMadeAtYear2000OrMoreRecent = rateService
                .ratedByUserAtYearOrMoreRecent(userId, productionYear);

        // Verify State
        assertEquals(1, ratesByUserOfFilmsMadeAtYear2000OrMoreRecent.size());
        assertTrue(ratesByUserOfFilmsMadeAtYear2000OrMoreRecent.contains(rateOfFrozenByUser));
    }

    @Test
    void whenAFilmIsRatedMoreThan10Times_ItShouldNotifyOnceThatItHasBeenLikedBy10DifferentUsers() {
        final String title = "aTitle";
        final List<Rate> ratesForFilm = randomListOfRatesOfSizeForFilm(RateService.RATES_PER_FILM_FOR_NOTIFICATION, title);

        // Exercise
        ratesForFilm.forEach(rateService::save);

        // Verify Spy
        Mockito.verify(likedNotifier, Mockito.times(1)).notifyForFilm(title);
    }

    private void saveRatesIntoService(List<Rate> rates) {
        rates.forEach(rate -> repository.save(rate.id, rate));
    }
}
