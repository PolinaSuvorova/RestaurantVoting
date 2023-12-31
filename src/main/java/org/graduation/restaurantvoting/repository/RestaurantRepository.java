package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "JOIN MenuItem d on d.restaurant.id = r.id  " +
            "where r.id =:id and d.dateMenu =:dateMenu ")
    Restaurant get(int id, LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "JOIN MenuItem d on d.restaurant.id = r.id " +
            "WHERE d.dateMenu =:dateMenu ORDER BY r.name")
    List<Restaurant> getActiveForDate(LocalDate dateMenu);

    @Query("SELECT r FROM Restaurant r " +
            "WHERE r.name =:name ORDER BY r.name")
    Restaurant getAllByName(String name);

}
