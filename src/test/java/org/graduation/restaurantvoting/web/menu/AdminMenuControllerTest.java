package org.graduation.restaurantvoting.web.menu;

import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.MenuItem;
import org.graduation.restaurantvoting.repository.MenuItemRepository;
import org.graduation.restaurantvoting.util.JsonUtil;
import org.graduation.restaurantvoting.web.AbstractControllerTest;
import org.graduation.restaurantvoting.web.MenuTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.graduation.restaurantvoting.web.MenuTestData.*;
import static org.graduation.restaurantvoting.web.RestaurantTestData.RESTAURANT1_ID;
import static org.graduation.restaurantvoting.web.RestaurantTestData.RESTAURANT3_ID;
import static org.graduation.restaurantvoting.web.TestUtil.userHttpBasic;
import static org.graduation.restaurantvoting.web.UserTestData.admin;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> menuItemRepository.getExisted(DISH1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID + "/dishes/" + MenuTestData.NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes/" + +DISH1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEM_1));
    }


    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes/" + +DISH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes/" + +MenuTestData.NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEMS_REST_1));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID + "/dishes/filter")
                .param("startDate", "2023-08-08")
                .param("endDate", "2023-08-08")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(TO_MATCHER.contentJson(MENU_ITEMS_HISTORY_REST_1));
    }

    @Test
    void update() throws Exception {
        MenuItem updated = MenuTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.get(DISH1_ID, RESTAURANT1_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = MenuTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT3_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newMenuItem)))
                .andExpect(status().isCreated());

        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.get(newId, RESTAURANT3_ID), newMenuItem);
    }

    @Test
    void createWithLocationDuplicateKey() throws Exception {
        MenuItem newMenuItem = MenuTestData.getNew();
        newMenuItem.setName(MENU_ITEM_1.getName());
        perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT1_ID + "/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newMenuItem)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(MenuValidator.EXCEPTION_DUPLICATE_DISH)));
    }

    @Test
    void updateWithLocationDuplicateKey() throws Exception {
        MenuItem updMenuItem = MenuTestData.getUpdated();
        updMenuItem.setName(MENU_ITEM_2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID + "/dishes/" + DISH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(updMenuItem)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(MenuValidator.EXCEPTION_DUPLICATE_DISH)));
    }
}