package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.MenuItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {
    @Query("SELECT d FROM MenuItem d " +
            "WHERE d.restaurant.id=:restaurantId AND " +
            "d.dateMenu >= :startDate AND " +
            "d.dateMenu <= :endDate ORDER BY d.dateMenu DESC")
    List<MenuItem> getBetween(int restaurantId,
                              LocalDate startDate,
                              LocalDate endDate);

    @Query("SELECT count(d.id) FROM MenuItem d " +
            "WHERE d.restaurant.id=:restaurantId AND " +
            "d.dateMenu = :localDate ")
    int countMenuItem(int restaurantId,
                      LocalDate localDate);

    @Query("SELECT d FROM MenuItem d " +
            "WHERE d.restaurant.id=:restaurantId ORDER BY d.dateMenu DESC")
    List<MenuItem> getAllByRestId(int restaurantId);


    @Query("SELECT d FROM MenuItem d " +
            "WHERE d.id= :id and d.restaurant.id=:restaurantId")
    MenuItem get(int id, int restaurantId);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem d WHERE d.id=:id and d.restaurant.id=:restaurantId")
    int delete(int id, int restaurantId);

    //  https://stackoverflow.com/a/60695301/548473 (existed delete code 204, not existed: 404)
    default void deleteExisted(int id, int restaurantId) {
        if (delete(id, restaurantId) == 0) {
            throw new NotFoundException("Entity with id=" + id + " not found");
        }
    }
}
