package org.graduation.restaurantvoting.web.restaurant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for view restaurants")
@Slf4j
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    @ApiOperation("Get restaurants for current date")
    public List<Restaurant> getActiveForCurrentDate() {
        log.info("getActiveForDate");
        LocalDate menuDate = LocalDate.now();
        return restaurantService.getActiveAndFilterByName(null, menuDate);
    }

    @GetMapping("/filter")
    @ApiOperation("Get restaurants for current date with filter by name")
    public List<Restaurant> getActiveForWithFilter(@RequestParam @Nullable String name) {
        log.info("getActiveForDate");
        LocalDate menuDate = LocalDate.now();
        return restaurantService.getActiveAndFilterByName(name, menuDate);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get restaurant by id for current date")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return restaurantService.get(id);
    }
}
