package org.graduation.restaurantvoting.web.menu;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.MenuItem;
import org.graduation.restaurantvoting.repository.MenuItemRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.util.DateTimeUtil;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
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
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for admin by managed menu for day")
@Slf4j
public class AdminMenuController {

    public static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    private MenuValidator menuValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(menuValidator);
    }

    @Autowired
    private MenuItemRepository repository;

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
    public MenuItem get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get MenuItem {} ", id);
        Optional<MenuItem> dish = Optional.ofNullable(repository.get(id, restaurantId));
        return dish.orElseThrow(() -> new NotFoundException("MenuItem with id=" + id + " not found"));
    }

    @GetMapping("/{restaurantId}/dishes")
    @ApiOperation("Get all dishes for restaurant")
    public List<MenuItem> getAll(@PathVariable int restaurantId) {
        log.info("get all");
        return repository.getAllByRestId(restaurantId);
    }

    @GetMapping("/{restaurantId}/dishes/filter")
    @ApiOperation("Get all dishes for restaurant with filter by date")
    public List<MenuItem> getBetween(
            @PathVariable int restaurantId,
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalDate endDate) {
        log.info("filter dishes");
        return repository.getBetween(restaurantId,
                DateTimeUtil.setLocDateOrMin(startDate),
                DateTimeUtil.setLocDateOrMax(endDate));
    }

    @PutMapping(value = "/{restaurantId}/dishes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update menuItem for restaurant if menuItem create in current date")
    public void update(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update menuItem {} with id={}", menuItem, id);
        ValidationUtil.assureIdConsistent(menuItem, id);
        if (repository.get(id, restaurantId) == null) {
            ValidationUtil.checkNotFoundWithId(null, id);
        }
        save(menuItem);
    }

    @PostMapping(value = "/{restaurantId}/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Create menuItem for restaurant")
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItem menuItem, @PathVariable int restaurantId) {
        log.info("create menuItem {} ", menuItem);
        ValidationUtil.checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepository.getReferenceById(restaurantId));
        ValidationUtil.assureIdConsistent(menuItem.getRestaurant(), restaurantId);
        MenuItem created = save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    private MenuItem save(MenuItem menuItem) {
        return repository.save(menuItem);
    }

}
