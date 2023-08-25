package org.graduation.restaurantvoting.web.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.User;
import org.graduation.restaurantvoting.to.UserTo;
import org.graduation.restaurantvoting.util.UsersUtil;
import org.graduation.restaurantvoting.web.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.graduation.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static org.graduation.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api("Api for mange profile")
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    @GetMapping
    @ApiOperation("Get user")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("delete user")
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Register new user")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = userService.create(UsersUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @ApiOperation("Update user")
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        userService.update(UsersUtil.updateFromTo(user, userTo));
    }
}
