package kata.domain.rate;

import kata.domain.user.UserId;
import support.Repository;

import java.util.List;

public interface RateRepository extends Repository<Rate, RateId> {
    List<Rate> all();

    List<Rate> ratesForFilm(String title);
    
    List<Rate> ratesForUser(UserId id);
    
    void save(Rate rate);
}
