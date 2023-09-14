package org.graduation.restaurantvoting.service;

import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final CacheManager cacheManager;

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository, CacheManager cacheManager) {
        this.repository = repository;
        this.cacheManager = cacheManager;
    }

    @CachePut(value = "restaurants", key = "#result.id")
    public Restaurant create(Restaurant r) {
        Assert.notNull(r, "restaurant must not be null");
        ValidationUtil.checkNew(r);
        return repository.save(r);
    }

    @CacheEvict(cacheNames = "restaurants", key = "#id")
    public void delete(int id) {
        repository.deleteExisted(id);
    }

    @Cacheable(cacheNames = "restaurants", key = "#id")
    public Restaurant get(int id) {
        Optional<Restaurant> restaurantBb = Optional.ofNullable(repository.getExisted(id));
        return restaurantBb.orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }

    public List<Restaurant> getActiveAndFilterByName(String name, LocalDate dateMenu) {
        return getActiveForDate(dateMenu).stream()
                .filter(restaurant -> (restaurant.getName().equals(name) || name == null))
                .toList();
    }

    private List<Restaurant> getActiveForDate(LocalDate dateMenu) {
        List<Restaurant> restaurants = repository.getActiveForDate(dateMenu);
        Cache cache = cacheManager.getCache("restaurants");
        if (cache != null) {
            for (Restaurant r : restaurants) {
                assert r.getId() != null;
                cache.putIfAbsent(r.getId(), r);
            }
        }
        return restaurants;
    }

    public List<Restaurant> getAll() {
        List<Restaurant> restaurants = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        Cache cache = cacheManager.getCache("restaurants");

        if (cache != null) {
            for (Restaurant r : restaurants) {
                assert r.getId() != null;
                cache.putIfAbsent(r.getId(), r);
            }
        }
        return restaurants;
    }

    @CachePut(cacheNames = "restaurants", key = "#r.id")
    public Restaurant update(Restaurant r) {
        Assert.notNull(r, "restaurant must not be null");
        return repository.save(r);
    }
}
