package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d " +
            "WHERE d.restaurant.id=:restaurantId AND " +
            "d.dateMenu >= :startDate AND " +
            "d.dateMenu <= :endDate ORDER BY d.dateMenu DESC")
    List<Dish> getBetween(@Param("restaurantId") int restaurantId,
                          @Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM Dish d " +
            "WHERE d.restaurant.id=:restaurantId ORDER BY d.dateMenu DESC")
    List<Dish> getAllByRestId(@Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d " +
            "WHERE d.id= :id and d.restaurant.id=:restaurantId")
    Dish get( @Param("id") int id,
              @Param("restaurantId") int restaurantId);
}
