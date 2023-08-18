package org.graduation.restaurantvoting.repository;

import org.apache.el.stream.Optional;
import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT m FROM Restaurant m " +
            "WHERE m.name = :name ORDER BY m.name DESC")
    List<Restaurant> getAllByName(String name);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.id IN (select d.restaurant.id from Dish d where " +
            "d.dateMenu =:dateMenu ) ORDER BY r.name" )
    List<Restaurant> getActiveForDate(LocalDate dateMenu);

}
