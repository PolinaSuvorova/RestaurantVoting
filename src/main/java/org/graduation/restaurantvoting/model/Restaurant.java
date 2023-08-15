package org.graduation.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurant",
        indexes = @Index(name = "RESTAURANT_NAME_INDEX",
                columnList = "NAME", unique = true))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("dateMenu desc")
    @JsonManagedReference
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name) {
        super(id, name);
        dishes = new ArrayList<>();
    }

    public Restaurant(Integer id, String name, List<Dish> dishes) {
        super(id, name);
        this.dishes = dishes;
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.dishes);
    }
}
