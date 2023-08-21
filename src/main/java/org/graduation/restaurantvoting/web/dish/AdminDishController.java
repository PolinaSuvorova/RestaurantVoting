package org.graduation.restaurantvoting.web.dish;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.Dish;
import org.graduation.restaurantvoting.repository.DishRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.util.DateTimeUtil;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for admin by managed menu for day")
public class AdminDishController {

    public static final String REST_URL = "/api/admin/restaurants";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DishValidator dishValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(dishValidator);
    }

    @Autowired
    private DishRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @DeleteMapping("/{restaurantId}/dishes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete dish from restaurant by id")
    public void delete(@PathVariable int restaurantId, @Valid @PathVariable int id) {
        log.info("delete dish {}", id);
        repository.deleteExisted(id, restaurantId);
    }

    @GetMapping("/{restaurantId}/dishes/{id}")
    @ApiOperation("Get dish by restaurant id and dish id")
    public Dish get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get Dish {} ", id);
        Optional<Dish> dish = Optional.ofNullable(repository.get(id, restaurantId));
        return dish.orElseThrow(() -> new NotFoundException("Dish with id=" + id + " not found"));
    }

    @GetMapping("/{restaurantId}/dishes")
    @ApiOperation("Get all dishes for restaurant")
    public List<Dish> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return repository.getAllByRestId(restaurantId);
    }

    @GetMapping("/{restaurantId}/dishes/filter")
    @ApiOperation("Get all dishes for restaurant with filter by date")
    public List<Dish> getBetween(
            @PathVariable int restaurantId,
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate) {
        log.info("filter dishes");
        return repository.getBetween(restaurantId,
                DateTimeUtil.atStartOfDayOrMin(startDate),
                DateTimeUtil.atStartOfDayOrMax(endDate));
    }

    @PutMapping(value = "/{restaurantId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update dish for restaurant if dish create in current date")
    public void update(@Valid @RequestBody Dish dish, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update dish {} with id={}", dish, id);
        ValidationUtil.assureIdConsistent(dish, id);
        ValidationUtil.assureIdConsistent(repository.getWithRestaurant(id, restaurantId).getRestaurant(), restaurantId);
        save(dish);
    }

    @PostMapping(value = "/{restaurantId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Create dish for restaurant")
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create dish {} ", dish);
        ValidationUtil.checkNew(dish);
        dish.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        ValidationUtil.assureIdConsistent(dish.getRestaurant(), restaurantId);
        Dish created = save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private Dish save(Dish dish) {
        return repository.save(dish);
    }

}
