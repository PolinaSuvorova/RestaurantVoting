package org.graduation.restaurantvoting.web.dish;

import jakarta.validation.Valid;
import org.graduation.restaurantvoting.model.Dish;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.DishRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    public static final String REST_URL = "/api/admin/";

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private DishRepository repository;
    private RestaurantRepository restaurantRepository;

    @GetMapping("/restaurants/{restaurantId}/dishes/{id}")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get Dish {} ", id);
        return repository.get(id,restaurantId);
    }

    @DeleteMapping("/restaurants/{restaurantId}/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @Valid @PathVariable int id) {
        log.info("delete dish {}", id);
        repository.delete(id);
    }

    @GetMapping("/restaurants/{restaurantId}/dishes")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return repository.getAllByRestId(restaurantId);
    }

    @GetMapping("/restaurants/{restaurantId}/dishes/filter")
    public List<Dish> getBetween(
            @RequestParam int restaurantId,
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate) {
        log.info("filter dishes");
        return repository.getBetween(restaurantId,startDate,endDate);
    }

    @PutMapping(value = "/restaurants/{restaurantId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update dish {} with id={}", dish, id);
        ValidationUtil.assureIdConsistent(dish, id);
        save(dish, restaurantId);
    }

    @PostMapping(value = "/restaurants/{restaurantId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create dish {} ", dish);
        ValidationUtil.checkNew(dish);
        Dish created = save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private Dish save(Dish dish, int restaurantId) {
        Restaurant restaurant = dish.getRestaurant();
        ValidationUtil.checkRestId(restaurant, restaurantId);
        return repository.save(dish);
    }

}
