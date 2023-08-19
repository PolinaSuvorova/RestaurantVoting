package org.graduation.restaurantvoting.web;

import org.graduation.restaurantvoting.model.Restaurant;

import java.util.List;

public class RestaurantTestData {
    private static int START_SEQ = 1;
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");
    public static MatcherFactory.Matcher<Restaurant> TO_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);

    public static final int NOT_FOUND = 10;
    public static final int RESTAURANT1_ID = START_SEQ;
    public static final int RESTAURANT2_ID = START_SEQ + 1;
    public static final int RESTAURANT3_ID = START_SEQ + 2;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Restaurant1");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "Restaurant2");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Restaurant3");

    public static final List<Restaurant> restaurants = List.of(restaurant1, restaurant2, restaurant3);
    public static final List<Restaurant> restaurantsActive = List.of(restaurant1, restaurant2);

    public static Restaurant getNew() {
        return new Restaurant(null, "New restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Updated restaurant");
    }
}