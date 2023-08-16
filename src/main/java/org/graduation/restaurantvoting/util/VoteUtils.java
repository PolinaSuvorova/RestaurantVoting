package org.graduation.restaurantvoting.util;

import org.graduation.restaurantvoting.model.Vote;
import org.graduation.restaurantvoting.to.VoteTo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VoteUtils {

    public static VoteTo convertTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getDateVote());
    }

    public static List<VoteTo> getTos(Collection<Vote> votes) {
        if (votes == null){
            return new ArrayList<>();
        }
        return votes.stream()
                .map(VoteUtils::convertTo)
                .toList();
    }
}
