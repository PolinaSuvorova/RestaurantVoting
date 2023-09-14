package org.graduation.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurant"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurant_unique_name_idx")}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityReference(alwaysAsId = true)
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("dateMenu desc")
    @JsonIgnore
    private List<MenuItem> menuItems;

    public Restaurant(Integer id, String name) {
        super(id, name);
        menuItems = null;
    }

    public Restaurant(Integer id, String name, List<MenuItem> menuItems) {
        super(id, name);
        this.menuItems = menuItems;
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.menuItems);
    }

}
