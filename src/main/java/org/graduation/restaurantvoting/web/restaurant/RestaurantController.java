package org.graduation.restaurantvoting.web.restaurant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.util.ClockHolder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for view restaurants")
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";
    private final Logger log = getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    @GetMapping
    @ApiOperation("Get restaurants for current date")
    public List<Restaurant> getActiveForCurrentDate() {
        log.info("getActiveForDate");
        LocalDate menuDate = LocalDate.now();
        return repository.getActiveForDate(menuDate);
    }

    @GetMapping("/filter")
    @ApiOperation("Get restaurants for current date with filter by name")
    public List<Restaurant> getActiveForWithFilter(@RequestParam @Nullable String name) {
        log.info("getActiveForDate");
        LocalDate menuDate = LocalDate.now();
        return repository.getAllByName(name, menuDate);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get restaurant by id for current date")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        Optional<Restaurant> restaurantBb = Optional.ofNullable(repository.get(id, LocalDate.now(ClockHolder.getClock())));
        return restaurantBb.orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
    }
}
