package kata.domain.rate;

import kata.domain.film.Film;
import kata.domain.film.FilmService;
import kata.domain.user.UserId;
import kata.domain.user.UserIdDummy;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static kata.domain.film.FilmDummy.randomFilm;
import static kata.domain.rate.RateDummy.*;

class RateServiceTest_Mock {
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
    void shouldReceiveFromRepository() {
        final Rate rate = Rate.of("aTitle", 4, UserIdDummy.randomUserId());

        // Setup Expectations
        Mockito.doReturn(Optional.of(rate)).when(repository).findById(rate.id);
    
        // Exercise
        final Optional<Rate> ratingFromRepo = rateService.findById(rate.id);
    
        // Verify expectations
        Mockito.verify(repository).findById(rate.id);
    
        // Verify State
        Assertions.assertEquals(rate, ratingFromRepo.get());
    }

    @Test
    void shouldReturnRatesMadeByAUser() {
        final UserId userId = UserId.of("aUser");
        final Rate rateOneByUser = randomRate().withUserId(userId).build();
        final Rate rateTwoByUser = randomRate().withUserId(userId).build();

        final List<Rate> allRates = randomListOfRatesOfSize(10);
        allRates.add(rateOneByUser);
        allRates.add(rateTwoByUser);

        // Setup Expectations
        Mockito.doReturn(allRates).when(repository).all();

        // Exercise
        final List<Rate> ratedByUser = rateService.findByUser(userId);

        // Verify expectations
        Mockito.verify(repository).all();

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

        // Setup Expectations
        Mockito.doReturn(allRates).when(repository).all();
        Mockito.doReturn(Optional.of(theLionKingMovieAsOldFilm)).when(filmService).findById(theLionKingTitle);
        Mockito.doReturn(Optional.of(frozenMovieAsNewerFilm)).when(filmService).findById(frozenTitle);
    
        // Exercise
        final List<Rate> ratesByUserOfFilmsMadeAtYear2000OrMoreRecent = rateService
                .ratedByUserAtYearOrMoreRecent(userId, productionYear);

        // Verify expectations
        Mockito.verify(repository).all();

        // Verify State
        assertEquals(1, ratesByUserOfFilmsMadeAtYear2000OrMoreRecent.size());
        assertTrue(ratesByUserOfFilmsMadeAtYear2000OrMoreRecent.contains(rateOfFrozenByUser));
    }

    @Test
    void whenAFilmIsRatedMoreThan10Times_ItShouldNotifyOnceThatItHasBeenLikedBy10DifferentUsers() {
        final String title = "aTitle";
        final Rate rate = randomRate().withTitle(title).build();
        final List<Rate> ratesForFilm = randomListOfRatesOfSizeForFilm(RateService.RATES_PER_FILM_FOR_NOTIFICATION, title);

        // Setup Expectations
        Mockito.doReturn(ratesForFilm).when(repository).ratesForFilm(title);

        // Exercise
        rateService.save(rate);
    
        Mockito.verify(repository).ratesForFilm(title);

        // Verify expectations
        Mockito.verify(likedNotifier, Mockito.times(1)).notifyForFilm(title);
    }
}
