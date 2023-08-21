package org.graduation.restaurantvoting.web;

import org.graduation.restaurantvoting.model.Dish;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class DishTestData {
    private static int START_SEQ = 1;
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant");
    public static MatcherFactory.Matcher<Dish> TO_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);

    public static final int NOT_FOUND = 100;
    public static final int DISH1_ID = START_SEQ;
    public static final int DISH2_ID = START_SEQ + 1;
    public static final int DISH3_ID = START_SEQ + 2;
    public static final int DISH_HISTORY_ID = START_SEQ + 3;
    public static final Dish dish1 = new Dish(DISH1_ID, "Dish1.1", 600.00, LocalDate.now());
    public static final Dish dish2 = new Dish(DISH2_ID, "Dish1.2", 200.00, LocalDate.now());
    public static final Dish dish3 = new Dish(DISH3_ID, "Dish2.1", 400.00, LocalDate.now());
    public static final Dish dishHistory = new Dish(DISH_HISTORY_ID, "Dish1.1", 600.00, LocalDate.of(2023, Month.AUGUST, 8));
    public static final List<Dish> dishesNowRest1 = List.of(dish1, dish2);

    public static final List<Dish> dishesRest1 = List.of(dish1, dish2, dishHistory);
    public static final List<Dish> dishesHistoryRest1 = List.of(dishHistory);

    public static Dish getNew() {
        return new Dish(null, "New Dish", 1000.00, LocalDate.now());
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Dish1.1 Updated", 600.00, LocalDate.now());
    }
}