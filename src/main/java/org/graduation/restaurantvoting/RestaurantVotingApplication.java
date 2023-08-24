package org.graduation.restaurantvoting;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@AllArgsConstructor
@EnableCaching    //подключение Spring Cache
public class RestaurantVotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantVotingApplication.class, args);
    }

}
