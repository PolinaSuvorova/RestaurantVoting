package org.graduation.restaurantvoting.web.restaurant;

import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";
    private final Logger log = getLogger(getClass());

    @Autowired
    private RestaurantRepository repository;

    @GetMapping
    public List<Restaurant> getActiveForCurrentDate( ) {
        log.info("getActiveForDate");
        LocalDate menuDate = LocalDate.now();
        return repository.getActiveForDate("", menuDate);
    }

    @GetMapping("/filter")
    public List<Restaurant> getActiveForWithFilter(@RequestParam @Nullable String name) {
        log.info("getActiveForDate");
        LocalDate menuDate = LocalDate.now();
        return repository.getActiveForDate(name, menuDate);
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }
}
