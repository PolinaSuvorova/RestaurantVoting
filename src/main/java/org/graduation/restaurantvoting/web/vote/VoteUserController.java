package org.graduation.restaurantvoting.web.vote;

import jakarta.validation.Valid;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.model.Vote;
import org.graduation.restaurantvoting.repository.DataJpaVoteRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.graduation.restaurantvoting.to.VoteTo;
import org.graduation.restaurantvoting.util.VoteUtils;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.graduation.restaurantvoting.web.AuthUser;
import org.graduation.restaurantvoting.web.user.UniqueMailValidator;
import org.slf4j.Logger;
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

import static org.graduation.restaurantvoting.util.validation.ValidationUtil.assureIdConsistent;
import static org.graduation.restaurantvoting.util.validation.ValidationUtil.checkNew;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = VoteUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteUserController {

    @Autowired
    private VoteValidator voteValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(voteValidator);
    }

    @Autowired
    private DataJpaVoteRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    private final Logger log = getLogger(getClass());
    public static final String REST_URL = "/api/votes";


    @GetMapping("/{id}")
    public VoteTo get(@PathVariable int id) {
        int userId = AuthUser.get().id();
        log.info("get meal {} for user {}", id, userId);
        return VoteUtils.convertTo(repository.get(id, userId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @RequestBody VoteTo voteTo, @PathVariable int id) {
        int userId = AuthUser.get().id();
        log.info("get meal {} for user {}", id, userId);
        ValidationUtil.assureIdConsistent(voteTo, id);
        repository.delete(id,userId);
    }

    @GetMapping
    public List<VoteTo> getAll() {
        int userId = AuthUser.get().id();
        log.info("getall {} for user", userId);
        return VoteUtils.getTos(repository.getAll(userId));
    }

    @GetMapping("/filter")
    public List<VoteTo> getByFilter(
            @RequestParam @Nullable LocalDate voteDateStart,@RequestParam @Nullable LocalDate voteDateEnd ) {
        int userId = AuthUser.get().id();
        log.info("getall by Date {}-{} for user {}",voteDateStart,voteDateEnd, userId);
        return VoteUtils.getTos(repository.getBetween(voteDateStart,voteDateEnd,userId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@Valid @RequestBody VoteTo voteTo) {
        log.info("create {}", voteTo);
        checkNew(voteTo);
        int userId = AuthUser.get().id();
        Vote created = repository.save(voteTo,userId,voteTo.getRestaurantId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtils.convertTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody VoteTo voteTo, @PathVariable int id) {
        log.info("update {} with id={}", voteTo, id);
        assureIdConsistent(voteTo, id);
        int userId = AuthUser.get().id();
        repository.save(voteTo,userId,voteTo.getRestaurantId());
    }

}
