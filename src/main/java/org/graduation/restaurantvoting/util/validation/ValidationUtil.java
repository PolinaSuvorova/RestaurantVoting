package org.graduation.restaurantvoting.util.validation;

import lombok.experimental.UtilityClass;
import org.graduation.restaurantvoting.HasId;
import org.graduation.restaurantvoting.error.IllegalRequestDataException;
import org.graduation.restaurantvoting.error.NotFoundException;
import org.graduation.restaurantvoting.model.Restaurant;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@UtilityClass
public class ValidationUtil {
    public static final LocalTime END_TIME_FOR_CHANGES = LocalTime.of(11, 0);

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

    public static void checkDate(LocalDate localDate) {
        if (localDate == null) {
            throw new IllegalRequestDataException("Date must not be null");
        } else if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalRequestDataException("Changes permitted only for current Date");
        }
    }

    public static void checkTime(LocalTime localTime) {
        if (localTime == null) {
            throw new IllegalRequestDataException("Time must not be null");
        } else if (!localTime.isBefore(END_TIME_FOR_CHANGES)) {
            throw new IllegalRequestDataException("Changes permitted only for current Date" + " from 00:00 to " + END_TIME_FOR_CHANGES);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }
}