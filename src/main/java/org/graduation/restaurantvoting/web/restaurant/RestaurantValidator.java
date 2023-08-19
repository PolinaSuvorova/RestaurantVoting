package org.graduation.restaurantvoting.web.restaurant;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.graduation.restaurantvoting.model.Dish;
import org.graduation.restaurantvoting.model.Restaurant;
import org.graduation.restaurantvoting.repository.DishRepository;
import org.graduation.restaurantvoting.repository.RestaurantRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDate;
import java.util.Map;

@Component
@AllArgsConstructor
public class RestaurantValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_RESTAURANT = "Restaurant with this name already exists";
    private final RestaurantRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Restaurant.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        int idParam;
        String idParamString = (String) pathVariables.get("id");
        if (StringUtils.hasText(idParamString)) {
            idParam = Integer.parseInt(idParamString);
        } else {
            idParam = 0;
        }

        Restaurant restaurant = (Restaurant) target;
        String name = restaurant.getName();
        if ( name == null || name.isEmpty() ){
            return;
        }

        if (repository.getAllByName( name )
                .stream()
                .anyMatch(d -> {
                    assert d.getId() != null;
                    return !(d.getId().equals(idParam));
                })) {
            errors.rejectValue("name", EXCEPTION_DUPLICATE_RESTAURANT);
        }
    }
}
