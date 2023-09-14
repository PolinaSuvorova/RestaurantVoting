package org.graduation.restaurantvoting.web.menu;

import org.graduation.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.graduation.restaurantvoting.web.MenuTestData.*;
import static org.graduation.restaurantvoting.web.RestaurantTestData.RESTAURANT1_ID;
import static org.graduation.restaurantvoting.web.RestaurantTestData.RESTAURANT3_ID;
import static org.graduation.restaurantvoting.web.TestUtil.userHttpBasic;
import static org.graduation.restaurantvoting.web.UserTestData.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MenuController.REST_URL + '/';

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEM_1));
    }

    @Test
    void getWithError() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + RESTAURANT3_ID + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getActiveForCurrentDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEMS_NOW_REST_1));
    }
}