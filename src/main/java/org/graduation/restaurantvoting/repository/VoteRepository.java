package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.dateVote DESC")
    List<Vote> getAll(int userId);

    @Query("SELECT v from Vote v WHERE v.user.id=:userId AND v.dateVote >= :startDate AND v.dateVote <= :endDate ORDER BY v.dateVote DESC")
    List<Vote> getBetween(LocalDate startDate, LocalDate endDate, int userId);

    @Query("SELECT v from Vote v WHERE v.id = :id and v.user.id=:userId")
    Vote get(int id, int userId);
}
