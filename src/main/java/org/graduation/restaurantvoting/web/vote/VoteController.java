package org.graduation.restaurantvoting.web.vote;

import org.graduation.restaurantvoting.repository.DataJpaVoteRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(value = VoteUserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {
    @Autowired
    private DataJpaVoteRepository repository;

    @Autowired
    private RestaurantRepository restaurantRepository;
    private final Logger log = getLogger(getClass());
    public static final String REST_URL = "/api/restaurants";

}
