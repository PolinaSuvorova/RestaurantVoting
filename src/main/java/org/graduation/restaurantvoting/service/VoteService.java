package org.graduation.restaurantvoting.service;

import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.model.Vote;
import org.graduation.restaurantvoting.repository.MenuItemRepository;
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
import java.util.Objects;

@Service
public class VoteService {
    private final VoteRepository repository;
    private final RestaurantRepository restaurantRepository;

    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository repository, RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

    public Vote get(int id, int userId) {
        Vote vote = repository.get(id, userId);
        ValidationUtil.checkNotFoundWithId(vote, id);
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
        return repository.getBetween(DateTimeUtil.setLocDateOrMin(startDate), DateTimeUtil.setLocDateOrMax(endDate), userId);
    }

    public List<Vote> getAll(int userId) {
        return repository.getAll(userId);
    }

    @Transactional
    public void update(VoteTo voteTo, int userId) {
        Vote vote = get(voteTo.getId(), userId);
        Integer restaurantId = voteTo.getRestaurantId();
        vote.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        vote.setTimeVote(LocalTime.now(ClockHolder.getClock()));
        checkDateTime(vote);
        checkRestaurant(vote, restaurantId);
        repository.save(vote);
    }

    @Transactional
    public Vote create(VoteTo voteTo, int userId) {
        Assert.notNull(voteTo, "vote must not be null");
        Integer restaurantId = voteTo.getRestaurantId();
        Vote vote = new Vote(null, LocalDate.now(ClockHolder.getClock()), userRepository.getReferenceById(userId), restaurantRepository.getReferenceById(restaurantId));
        checkDateTime(vote);
        checkRestaurant(vote, restaurantId);
        return repository.save(vote);
    }

    private void checkRestaurant(Vote vote, int restaurantId) {
        Restaurant restaurant = vote.getRestaurant();
        if (restaurant == null || !Objects.equals(restaurant.getId(), restaurantId)) {
            ValidationUtil.checkNotFoundWithId(null, restaurantId);
        }

        if (menuItemRepository.countMenuItem(restaurantId, vote.getDateVote()) == 0) {
            throw new NotFoundException("Not found restaurant with menu");
        }
    }

    private void checkDateTime(Vote vote) {
        ValidationUtil.checkDate(vote.getDateVote());
        ValidationUtil.checkTime(vote.getTimeVote());
    }

}
