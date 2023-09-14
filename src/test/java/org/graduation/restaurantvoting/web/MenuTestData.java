package org.graduation.restaurantvoting.web;

import org.graduation.restaurantvoting.model.MenuItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class MenuTestData {
    private static int START_SEQ = 1;
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "restaurant");
    public static MatcherFactory.Matcher<MenuItem> TO_MATCHER = MatcherFactory.usingEqualsComparator(MenuItem.class);

    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = START_SEQ;
    public static final int DISH2_ID = START_SEQ + 1;
    public static final int DISH3_ID = START_SEQ + 2;
    public static final int DISH_HISTORY_ID = START_SEQ + 3;
    public static final MenuItem MENU_ITEM_1 = new MenuItem(DISH1_ID, "Dish1.1", 600, LocalDate.now(), RestaurantTestData.restaurant1);
    public static final MenuItem MENU_ITEM_2 = new MenuItem(DISH2_ID, "Dish1.2", 200, LocalDate.now(), RestaurantTestData.restaurant1);
    public static final MenuItem MENU_ITEM_3 = new MenuItem(DISH3_ID, "Dish2.1", 400, LocalDate.now(), RestaurantTestData.restaurant2);
    public static final MenuItem MENU_ITEM_HISTORY = new MenuItem(DISH_HISTORY_ID, "Dish1.1", 600,
            LocalDate.of(2023, Month.AUGUST, 8), RestaurantTestData.restaurant1);
    public static final List<MenuItem> MENU_ITEMS_NOW_REST_1 = List.of(MENU_ITEM_1, MENU_ITEM_2);

    public static final List<MenuItem> MENU_ITEMS_REST_1 = List.of(MENU_ITEM_1, MENU_ITEM_2, MENU_ITEM_HISTORY);
    public static final List<MenuItem> MENU_ITEMS_HISTORY_REST_1 = List.of(MENU_ITEM_HISTORY);

    public static MenuItem getNew() {
        return new MenuItem(null, "New MenuItem", 1000, LocalDate.now(), RestaurantTestData.restaurant1);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(DISH1_ID, "Dish1.1 Updated", 600, LocalDate.now(), RestaurantTestData.restaurant1);
    }
}