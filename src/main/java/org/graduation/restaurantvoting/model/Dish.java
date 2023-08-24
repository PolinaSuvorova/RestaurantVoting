package org.graduation.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "dish",
        indexes = {@Index(name = "DISH_RESTAURANT_ID_DATE_MENU_NAME_INDEX",
                columnList = "RESTAURANT_ID , DATE_MENU, NAME", unique = true),
                @Index(name = "DISH_DATE_INDEX", columnList = "DATE_MENU, NAME", unique = true)}
)
public class Dish extends AbstractNamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Restaurant restaurant;

    @Column(name = "date_menu", nullable = false, columnDefinition = "DATE default CURRENT_DATE", updatable = false)
    @NotNull
    private LocalDate dateMenu;

    @Column(name = "price", nullable = false, columnDefinition = "Decimal(6,2)")
    @NotNull
    private Double price;

    public Dish(Integer id, String name, Double price, LocalDate dateMenu) {
        super(id, name);
        this.dateMenu = dateMenu;
        this.price = price;
    }

    public Dish(Integer id, String name, Double price, LocalDate dateMenu, Restaurant restaurant) {
        super(id, name);
        this.dateMenu = dateMenu;
        this.price = price;
        this.restaurant = restaurant;
    }
}

