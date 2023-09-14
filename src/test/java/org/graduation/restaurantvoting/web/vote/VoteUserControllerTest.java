package org.graduation.restaurantvoting.web.vote;

import org.graduation.restaurantvoting.service.VoteService;
import org.graduation.restaurantvoting.to.VoteTo;
import org.graduation.restaurantvoting.util.JsonUtil;
import org.graduation.restaurantvoting.util.VoteUtils;
import org.graduation.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.graduation.restaurantvoting.web.RestaurantTestData.*;
import static org.graduation.restaurantvoting.web.TestUtil.userHttpBasic;
import static org.graduation.restaurantvoting.web.UserTestData.*;
import static org.graduation.restaurantvoting.web.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class VoteUserControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteUserController.REST_URL;

    @Autowired
    private VoteService voteService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + '/' + VOTE1_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + '/' + NOTE_FOUND)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(VOTE_MATCHER.contentJson(votesUser));
    }

    @Test
    void getByFilter() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter")
                .param("voteDateStart", "2023-08-08")
                .param("voteDateEnd", "2023-08-08")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VOTE_MATCHER.contentJson(votesHistoryUser));
    }

    @Test
    void createWithLocation() throws Exception {
        VoteTo newVote = new VoteTo(RESTAURANT2_ID);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());

        VoteTo created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(VoteUtils.convertTo(voteService.get(newId, ADMIN_ID)), newVote);
    }

    @Test
    void update() throws Exception {
        VoteTo updated = new VoteTo(VOTE1_ID, RESTAURANT2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(VoteUtils.convertTo(voteService.get(VOTE1_ID, USER_ID)), updated);
    }

    @Test
    void updateRestaurantWithoutMenu() throws Exception {
        VoteTo updated = new VoteTo(VOTE1_ID, RESTAURANT3_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateByOtherUser() throws Exception {
        VoteTo updated = new VoteTo(VOTE1_ID, RESTAURANT2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOldData() throws Exception {
        VoteTo updated = new VoteTo(vote3.getId(), RESTAURANT1_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + VOTE1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}