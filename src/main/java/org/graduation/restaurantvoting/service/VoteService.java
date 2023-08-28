package org.graduation.restaurantvoting.service;

import org.graduation.restaurantvoting.model.Vote;
import org.graduation.restaurantvoting.repository.DishRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.repository.UserRepository;
import org.graduation.restaurantvoting.repository.VoteRepository;
import org.graduation.restaurantvoting.to.VoteTo;
import org.graduation.restaurantvoting.util.ClockHolder;
import org.graduation.restaurantvoting.util.DateTimeUtil;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.graduation.restaurantvoting.util.validation.ValidationUtil.checkNotFoundWithId;

@Service
public class VoteService {
    private final VoteRepository repository;
    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository repository, RestaurantRepository restaurantRepository, DishRepository dishRepository, UserRepository userRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
    }

    public Vote get(int id, int userId) {
        Vote vote = repository.getExisted(id);
        Assert.isTrue(vote.getUser().getId().equals(userId), "Only owner can change vote");
        return vote;
    }

    @Transactional
    public void delete(int id, int userId) {
        Vote vote = get(id, userId);
        ValidationUtil.checkDate(vote.getDateVote());
        ValidationUtil.checkTime(LocalTime.now(ClockHolder.getClock()));
        repository.deleteExisted(id);
    }


    public List<Vote> getBetween(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getBetween(DateTimeUtil.atStartOfDayOrMin(startDate), DateTimeUtil.atStartOfDayOrMax(endDate), userId);
    }

    public List<Vote> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Transactional
    public void update(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "vote must not be null");
        Assert.isTrue(voteTo.getUserId().equals(userId), "Only owner can change vote");
        Vote vote = get(voteTo.getId(), userId);
        Integer restaurantId = voteTo.getRestaurantId();
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        vote.setTimeVote(LocalTime.now(ClockHolder.getClock()));
        check(vote, restaurantId);
        checkNotFoundWithId(repository.save(vote), voteTo.id());
    }

    @Transactional
    public Vote create(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "meal must not be null");
        Integer restaurantId = voteTo.getRestaurantId();
        Vote vote = new Vote(null, voteTo.getVoteDate(), userRepository.getReferenceById(voteTo.getUserId()), restaurantRepository.getReferenceById(restaurantId));
        check(vote, restaurantId);
        return repository.save(vote);
    }

    private void check(Vote vote, int restaurantId) {
        ValidationUtil.checkDate(vote.getDateVote());
        ValidationUtil.checkTime(vote.getTimeVote());

        checkNotFoundWithId(vote.getRestaurant(), restaurantId);
        if (dishRepository.isExistDate(restaurantId, vote.getDateVote()) == 0) {
            throw new IllegalArgumentException("Not found restaurant with menu");
        }
    }
}
