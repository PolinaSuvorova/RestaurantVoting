package org.graduation.restaurantvoting.web;

import org.graduation.restaurantvoting.to.VoteTo;
import org.graduation.restaurantvoting.util.ClockHolder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import static org.graduation.restaurantvoting.web.RestaurantTestData.*;
import static org.graduation.restaurantvoting.web.UserTestData.*;
public class VoteTestData {
    private static final int START_SEQ = 1;
    public static final MatcherFactory.Matcher<VoteTo> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);
    public static final int NOT_FOUND = 10;
    public static final int VOTE1_ID = START_SEQ;
    public static final int VOTE2_ID = START_SEQ + 1;
    public static final int VOTE3_ID = START_SEQ + 2;
    public static final int NOTE_FOUND = 100;
    public static final VoteTo vote1 = new VoteTo(VOTE1_ID, USER_ID, RESTAURANT1_ID,LocalDate.now(ClockHolder.getClock()));
    public static final VoteTo vote2 = new VoteTo(VOTE2_ID, ADMIN_ID, RESTAURANT2_ID,LocalDate.now(ClockHolder.getClock()));
    public static final VoteTo vote3 = new VoteTo(VOTE3_ID, USER_ID, RESTAURANT2_ID,LocalDate.of(2023, Month.AUGUST, 8));

    public static final List<VoteTo> votesUser = List.of(vote1, vote3);
    public static final List<VoteTo> votesActiveUser = List.of(vote1);
    public static final List<VoteTo> votesHistoryUser = List.of(vote3);

    public static VoteTo getNew() {
        return new VoteTo(null, ADMIN_ID, RESTAURANT2_ID, LocalDate.now(ClockHolder.getClock()));
    }

    public static VoteTo getUpdated() {
        return new VoteTo(VOTE1_ID, USER_ID, RESTAURANT2_ID,LocalDate.now(ClockHolder.getClock()));
    }
}