package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.name =:name and r.id IN (select d.restaurant.id from Dish d where " +
            "d.dateMenu =:dateMenu ) ORDER BY r.name")
    List<Restaurant> getAllByName(String name, LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.id =:id and r.id in (select d.restaurant.id from Dish d where " +
            "d.dateMenu =:dateMenu )")
    List<Restaurant> get(int id, LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.id IN (select d.restaurant.id from Dish d where " +
            "d.dateMenu =:dateMenu ) ORDER BY r.name")
    List<Restaurant> getActiveForDate(LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.name =:name ORDER BY r.name")
    List<Restaurant> getAllByName(String name);

}
