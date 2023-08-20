package org.graduation.restaurantvoting.to;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.beans.ConstructorProperties;
import java.time.LocalDate;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class VoteTo extends BaseTo {

    @NotNull
    Integer userId;

    @NotNull
    Integer restaurantId;

    @NotNull
    LocalDate voteDate;

    @ConstructorProperties({"id", "userId", "restaurantId", "voteDate"})
    public VoteTo(Integer id, Integer userId, Integer restaurantId, LocalDate voteDate) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.voteDate = voteDate;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public String toString() {
        return "VoteTo:" + id + '[' + restaurantId + ']' + voteDate;
    }
}
