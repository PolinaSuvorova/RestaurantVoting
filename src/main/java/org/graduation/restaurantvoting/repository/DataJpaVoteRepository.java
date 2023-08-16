package org.graduation.restaurantvoting.repository;

import org.graduation.restaurantvoting.model.Vote;
import org.graduation.restaurantvoting.to.VoteTo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class DataJpaVoteRepository {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public DataJpaVoteRepository(VoteRepository crudVoteRepository,
                                 UserRepository crudUserRepository,
                                 RestaurantRepository restaurantRepository) {
        this.voteRepository = crudVoteRepository;
        this.userRepository = crudUserRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Vote save(VoteTo voteTo, int userId, int restaurantId) {
        if (!voteTo.isNew() && get(voteTo.id(), userId) == null) {
            return null;
        }
        Vote vote = new Vote(voteTo.id(),
                             userRepository.getReferenceById(userId),
                             restaurantRepository.getReferenceById(userId),
                             voteTo.getVoteDate(),
                             LocalTime.now());
        return voteRepository.save(vote);
    }

    public boolean delete(int id, int userId) {
        return voteRepository.delete(id, userId) != 0;
    }

    public Vote get(int id, int userId) {
        return voteRepository.findById(id)
                .filter(vote -> vote.getUser().getId() == userId)
                .orElse(null);
    }

    public List<Vote> getAll(int userId) {
        return voteRepository.getAll(userId);
    }

    public List<Vote> getBetween(LocalDate startDate, LocalDate endDate, int userId) {
        return voteRepository.getBetween(startDate, endDate, userId);
    }

    public Vote getWithUser(int id, int userId) {
        return voteRepository.getWithUser(id, userId);
    }
}
