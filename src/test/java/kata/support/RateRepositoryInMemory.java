package kata.support;

import kata.domain.rate.Rate;
import kata.domain.rate.RateId;
import kata.domain.rate.RateRepository;
import kata.domain.user.UserId;

import java.util.*;
import java.util.stream.Collectors;

public class RateRepositoryInMemory implements RateRepository {
    private final Map<RateId, Rate> db = new HashMap<>();

    @Override
    public void save(RateId id, Rate rate) {
        db.put(id, rate);
    }

    @Override
    public Optional<Rate> findById(RateId id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Rate> all() {
        return new ArrayList<>(db.values());
    }

    @Override
    public List<Rate> ratesForFilm(String title) {
        return all().stream().filter(rate -> rate.title.equals(title)).collect(Collectors.toList());
    }
    
    @Override
    public List<Rate> ratesForUser(UserId id) {
        return all().stream().filter(rate -> rate.userId.equals(id)).collect(Collectors.toList());
    }
    
    @Override
    public void save(Rate rate) {
        save(rate.id, rate);
    }
}
