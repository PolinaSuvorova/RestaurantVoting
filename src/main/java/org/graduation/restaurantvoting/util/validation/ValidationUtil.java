package org.graduation.restaurantvoting.util.validation;

import lombok.experimental.UtilityClass;
import org.graduation.restaurantvoting.HasId;
import org.graduation.restaurantvoting.error.IllegalRequestDataException;
import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static ResponseEntity<String> prepareErrorFieldsMsg(BindingResult result) {
        String errorFieldsMsg = result.getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
        return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
    }

    public static void checkRestId(Restaurant been, int restaurantId) {
        if (been == null) {
            throw new IllegalRequestDataException(been.getClass().getSimpleName() + " id of restaurant must not be null");
        } else if (been.getId() != restaurantId) {
            throw new IllegalRequestDataException(been.getClass().getSimpleName() + " id of restaurant must has id=" + restaurantId);

        }
    }
}