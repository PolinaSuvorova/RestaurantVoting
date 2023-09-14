package org.graduation.restaurantvoting.web.restaurant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import static org.graduation.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for mange restaurants")
@Slf4j
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    private RestaurantValidator restaurantValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(restaurantValidator);
    }


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RestaurantService restaurantService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete restaurant")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        restaurantService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get restaurant without dish of menu")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }

    @GetMapping
    @ApiOperation("Get restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    @GetMapping("/filter")
    @ApiOperation("Get restaurants by filter( name, startDate,endDate")
    public List<Restaurant> getByFilter(
            @RequestParam @Nullable String name,
            @RequestParam @Nullable LocalDate menuDate) {
        log.info("getByFilter");
        return restaurantService.getActiveAndFilterByName(name, menuDate);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Create restaurant")
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update restaurant")
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }
}
