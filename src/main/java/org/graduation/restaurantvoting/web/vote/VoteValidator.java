package org.graduation.restaurantvoting.web.vote;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.graduation.restaurantvoting.repository.DataJpaVoteRepository;
import org.graduation.restaurantvoting.to.VoteTo;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalTime;

@Component
@AllArgsConstructor
public class VoteValidator implements Validator {

    private static LocalTime END_TIME_FOR_VOTE = LocalTime.of(11, 00, 00);

    public static final String EXCEPTION_DUPLICATE_DATE_VOTE = "Vote for User with this Voting date already exists";
    public static final String EXCEPTION_END_TIME_VOTE = "Vote after " + END_TIME_FOR_VOTE + " is not permitted";

    private final DataJpaVoteRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return VoteTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        errors.rejectValue("voteDate", "", EXCEPTION_DUPLICATE_DATE_VOTE);

        errors.rejectValue("voteDate", "", EXCEPTION_END_TIME_VOTE);
    }
}
