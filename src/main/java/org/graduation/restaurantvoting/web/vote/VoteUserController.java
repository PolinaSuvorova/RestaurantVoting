package org.graduation.restaurantvoting.web.vote;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.Vote;
import org.graduation.restaurantvoting.service.VoteService;
import org.graduation.restaurantvoting.to.VoteTo;
import org.graduation.restaurantvoting.util.VoteUtils;
import org.graduation.restaurantvoting.web.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.graduation.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static org.graduation.restaurantvoting.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = VoteUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("Api for restaurant voting ")
@Slf4j
public class VoteUserController {
    public static final String REST_URL = "/api/votes";

    @Autowired
    private VoteService voteService;

    @GetMapping("/{id}")
    @ApiOperation("Get vote by id for user")
    public VoteTo get(@PathVariable int id) {
        int userId = AuthUser.get().id();
        log.info("get meal {} for user {}", id, userId);
        return VoteUtils.convertTo(voteService.get(id, userId));
    }

    @GetMapping
    @ApiOperation("Get all votes for user")
    public List<VoteTo> getAll() {
        int userId = AuthUser.get().id();
        log.info("getall {} for user", userId);
        return VoteUtils.getTos(voteService.getAll(userId));
    }

    @GetMapping("/filter")
    @ApiOperation("Get Votes by filter")
    public List<VoteTo> getByFilter(
            @RequestParam @Nullable LocalDate voteDateStart, @RequestParam @Nullable LocalDate voteDateEnd) {
        int userId = AuthUser.get().id();
        log.info("getall by Date {}-{} for user {}", voteDateStart, voteDateEnd, userId);
        return VoteUtils.getTos(voteService.getBetween(voteDateStart, voteDateEnd, userId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Create Vote")
    public ResponseEntity<VoteTo> createWithLocation(@Valid @RequestBody VoteTo voteTo) {
        log.info("create {}", voteTo);
        checkNew(voteTo);
        int userId = AuthUser.get().id();
        Vote created = voteService.create(voteTo, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtils.convertTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update Vote")
    public void update(@Valid @RequestBody VoteTo voteTo, @PathVariable int id) {
        log.info("update {} with id={}", voteTo, id);
        assureIdConsistent(voteTo, id);
        int userId = AuthUser.get().id();
        voteService.update(voteTo, userId);
    }
}
