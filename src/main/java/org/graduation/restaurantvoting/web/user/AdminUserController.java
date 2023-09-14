package org.graduation.restaurantvoting.web.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.graduation.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static org.graduation.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for admin for manage user")
@Slf4j
public class AdminUserController extends AbstractUserController {
    static final String REST_URL = "/api/admin/users";

    @Override
    @GetMapping("/{id}")
    @ApiOperation("Get user by id")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete user by id")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping
    @ApiOperation("Get all users")
    public List<User> getAll() {
        log.info("getAll");
        return userService.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Create user")
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        User created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL + "/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update user")
    public void update(@Valid @RequestBody User user, @PathVariable int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }

    @GetMapping("/by-email")
    @ApiOperation("Get user by email")
    public User getByEmail(@RequestParam String email) {
        log.info("getByEmail {}", email);
        return userService.getByEmail(email);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Set enable/disable for user")
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        userService.enable(id, enabled);
    }
}
