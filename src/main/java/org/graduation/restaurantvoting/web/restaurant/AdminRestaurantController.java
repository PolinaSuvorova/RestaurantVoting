package org.graduation.restaurantvoting.web.restaurant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.graduation.restaurantvoting.error.IllegalRequestDataException;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
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
import static org.graduation.restaurantvoting.util.validation.ValidationUtil.checkNew;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for mange restaurants")
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";
    private final Logger log = getLogger(getClass());

    @Autowired
    private RestaurantValidator restaurantValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(restaurantValidator);
    }


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RestaurantRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete restaurant")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get restaurant without dish of menu")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.getExisted(id);
    }

    @GetMapping
    @ApiOperation("Get restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/filter")
    @ApiOperation("Get restaurants by filter( name, startDate,endDate")
    public List<Restaurant> getByFilter(
            @RequestParam @Nullable String name,
            @RequestParam @Nullable LocalDate menuDate) {
        log.info("getByFilter");
        if (menuDate != null) {
            return repository.getAllByName(name, menuDate);
        } else {
            return repository.getActiveForDate(menuDate);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Create restaurant")
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
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
        try {
            repository.save(restaurant);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalRequestDataException(messageSource.getMessage("Restaurant with name already is exist", null, LocaleContextHolder.getLocale()));
        }
    }
}
