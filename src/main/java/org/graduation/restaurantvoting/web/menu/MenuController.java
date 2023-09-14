package org.graduation.restaurantvoting.web.menu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.MenuItem;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.MenuItemRepository;
import org.graduation.restaurantvoting.service.RestaurantService;
import org.graduation.restaurantvoting.util.ClockHolder;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for user by watch")
@Slf4j
public class MenuController {

    public static final String REST_URL = "/api/restaurants/";

    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{restaurantId}/dishes/{id}")
    @ApiOperation("Get dish for restaurant on current day")
    public MenuItem get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get MenuItem {} ", id);
        MenuItem menuItem = repository.getExisted(id);
        ValidationUtil.checkNotFoundWithId(menuItem, id);
        Restaurant restaurant = menuItem.getRestaurant();
        if (restaurant == null || !Objects.equals(restaurant.getId(), restaurantId) ||
                (menuItem.getDateMenu().isBefore(LocalDate.now(ClockHolder.getClock())))) {
            ValidationUtil.checkNotFoundWithId(null, id);
        }
        return menuItem;
    }

    @GetMapping("/{restaurantId}/dishes")
    @ApiOperation("Get dishes for restaurant on current day")
    public List<MenuItem> getActiveForCurrentDate(@PathVariable int restaurantId) {
        log.info("get all");
        return repository.getBetween(restaurantId, LocalDate.now(ClockHolder.getClock()), LocalDate.now(ClockHolder.getClock()));
    }

}
