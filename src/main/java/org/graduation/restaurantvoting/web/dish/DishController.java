package org.graduation.restaurantvoting.web.dish;

import org.graduation.restaurantvoting.model.Dish;
import org.graduation.restaurantvoting.repository.DishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    public static final String REST_URL = "/api/restaurants/";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishRepository repository;

    public DishController(DishRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{restaurantId}/dishes/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get Dish {} ", id);
        Dish dish = repository.get(id, restaurantId);
        return dish;
    }

    @GetMapping("/{restaurantId}/dishes")
    public List<Dish> getActiveForCurrentDate(@PathVariable int restaurantId) {
        log.info("get all");
        return repository.getBetween(restaurantId, LocalDate.now(), LocalDate.now());
    }

}
