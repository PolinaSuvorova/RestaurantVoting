package org.graduation.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurant"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurans_unique_name_idx")}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("dateMenu desc")
    @JsonManagedReference
    @JsonIgnore
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
