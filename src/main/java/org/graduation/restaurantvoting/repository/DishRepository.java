package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.Dish;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d " +
            "WHERE d.restaurant.id=:restaurantId AND " +
            "d.dateMenu >= :startDate AND " +
            "d.dateMenu <= :endDate ORDER BY d.dateMenu DESC")
    List<Dish> getBetween(int restaurantId,
                          LocalDate startDate,
                          LocalDate endDate);

    @Query("SELECT d FROM Dish d " +
            "WHERE d.restaurant.id=:restaurantId ORDER BY d.dateMenu DESC")
    List<Dish> getAllByRestId(int restaurantId);


    @Query("SELECT d FROM Dish d " +
            "WHERE d.id= :id and d.restaurant.id=:restaurantId")
    Dish get(int id, int restaurantId);

    @Query("SELECT d FROM Dish d " +
            " join fetch Restaurant r where  d.id= :id and d.restaurant.id=:restaurantId")
    Dish getWithRestaurant(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id and d.restaurant.id=:restaurantId")
    int delete(int id, int restaurantId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    default void deleteExisted(int id, int restaurantId) {
        if (delete(id, restaurantId) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }
}
