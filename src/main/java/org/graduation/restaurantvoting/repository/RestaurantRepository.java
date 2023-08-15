package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT m FROM Restaurant m " +
            "WHERE m.name like '%name%' ORDER BY m.name DESC")
    List<Restaurant> getAllByName(@Param("name") String name);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE " +
            "r.id in (select d.restaurant.id from Dish d where " +
            "d.dateMenu =:dateMenu ) ORDER BY r.name" )
    List<Restaurant> getActiveForDate(@Param("dateMenu") LocalDate dateMenu);
}
