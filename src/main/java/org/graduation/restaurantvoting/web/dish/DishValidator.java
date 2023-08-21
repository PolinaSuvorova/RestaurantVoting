package org.graduation.restaurantvoting.web.dish;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.graduation.restaurantvoting.model.Dish;
import org.graduation.restaurantvoting.repository.DishRepository;
import org.graduation.restaurantvoting.util.ClockHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDate;
import java.util.Map;

@Component
@AllArgsConstructor
public class DishValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_DISH = "Dish by date with this name already exists";
    public static final String EXCEPTION_DATE_DISH = "Update/create  by this date is not permitted";
    public static final String EXCEPTION_DATE_UPDATE = "Update date is not permitted";

    private final DishRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Dish.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        String methodCall = request.getMethod();
        Dish dish = (Dish) target;

        LocalDate dateMenu = dish.getDateMenu();
        if (dateMenu == null && methodCall.equals("POST")) {
            dateMenu = LocalDate.now(ClockHolder.getClock());
        }

        if (dateMenu == null || !dateMenu.equals(LocalDate.now(ClockHolder.getClock()))) {
            errors.rejectValue("dateMenu", "", EXCEPTION_DATE_DISH);
        }

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String idStr = (String) pathVariables.get("restaurantId");
        if (!StringUtils.hasText(idStr)) {
            return;
        }
        int restaurantId = Integer.parseInt(idStr);

        int idParam;
        String idParamString = (String) pathVariables.get("id");
        if (StringUtils.hasText(idParamString)) {
            idParam = Integer.parseInt(idParamString);
            Dish dishDb = repository.get(idParam, restaurantId);
            if (!dishDb.getDateMenu().equals(dateMenu)) {
                errors.rejectValue("dateMenu", "", EXCEPTION_DATE_UPDATE);
            }
        } else {
            idParam = 0;
        }
        if (repository.getBetween(restaurantId, dateMenu, dateMenu)
                .stream()
                .anyMatch(d -> {
                    assert d.getId() != null;
                    return !(d.getId().equals(idParam)) && d.getName().equals(dish.getName());
                })) {
            errors.rejectValue("name", EXCEPTION_DUPLICATE_DISH);
        }
    }
}
