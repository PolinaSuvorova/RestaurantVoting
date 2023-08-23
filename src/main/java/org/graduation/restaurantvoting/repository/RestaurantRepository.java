package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT r FROM Restaurant r " +
            "JOIN Dish d on d.restaurant.id = r.id  " +
            "where r.name =:name and d.dateMenu =:dateMenu")
    List<Restaurant> getAllByName(String name, LocalDate dateMenu);

    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "JOIN Dish d on d.restaurant.id = r.id  " +
            "where d.dateMenu =:dateMenu ")
    Restaurant get(int id, LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "JOIN Dish d on d.restaurant.id = r.id " +
            "WHERE d.dateMenu =:dateMenu ORDER BY r.name")
    List<Restaurant> getActiveForDate(LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.name =:name ORDER BY r.name")
    List<Restaurant> getAllByName(String name);

}
