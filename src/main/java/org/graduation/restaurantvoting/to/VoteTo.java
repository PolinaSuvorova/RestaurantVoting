package org.graduation.restaurantvoting.to;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.beans.ConstructorProperties;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class VoteTo extends BaseTo {

    @NotNull
    Integer restaurantId;

    @ConstructorProperties({"id", "restaurantId"})
    public VoteTo(Integer id, Integer restaurantId) {
        super(id);
        this.restaurantId = restaurantId;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return "VoteTo:" + id + '[' + restaurantId + ']';
    }
}
