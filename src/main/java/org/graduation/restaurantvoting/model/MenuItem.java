package org.graduation.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu_item",
        indexes = {@Index(name = "menu_item_restaurant_id_date_menu_name_idx",
                columnList = "restaurant_id , date_menu, name", unique = true)}
)
public class MenuItem extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("restaurantId")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "date_menu", nullable = false, columnDefinition = "date default current_date", updatable = false)
    @NotNull
    private LocalDate dateMenu;

    @Column(name = "price", nullable = false, columnDefinition = "Integer")
    @NotNull
    private Integer price;

    public MenuItem(Integer id, String name, Integer price, LocalDate dateMenu, Restaurant restaurant) {
        super(id, name);
        this.dateMenu = dateMenu;
        this.price = price;
        this.restaurant = restaurant;
    }
}

