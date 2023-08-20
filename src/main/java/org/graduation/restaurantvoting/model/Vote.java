package org.graduation.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.graduation.restaurantvoting.util.ClockHolder;
import org.graduation.restaurantvoting.util.DateTimeUtil;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints =
        {
                @UniqueConstraint(columnNames = {"user_id", "date_vote"}, name = "vote_unique_user_date_idx"),
                @UniqueConstraint(columnNames = {"restaurant_id", "date_vote", "user_id"}, name = "vote_unique_restaurant_date_idx")
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
@ToString
public class Vote extends AbstractBaseEntity {

    @Column(name = "date_vote", nullable = false, columnDefinition = "DATE default CURRENT_DATE", updatable = false)
    @NotNull
    @DateTimeFormat(pattern = DateTimeUtil.DATE_PATTERN)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dateVote;

    @Column(name = "time_vote", nullable = false, columnDefinition = "TIME default CURRENT_TIME")
    @NotNull
    @DateTimeFormat(pattern = DateTimeUtil.TIME_PATTERN)
    private LocalTime timeVote = LocalTime.now(ClockHolder.getClock());

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private User user;

    public Vote(Integer id, @NotNull LocalDate dateVote, User user, Restaurant restaurant) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
        this.dateVote = dateVote;
    }

    @PrePersist
    public void beforePersist() {
        timeVote = LocalTime.now(ClockHolder.getClock());
        dateVote = LocalDate.now(ClockHolder.getClock());
    }

}
