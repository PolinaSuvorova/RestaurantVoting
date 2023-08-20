package org.graduation.restaurantvoting.web;

import org.graduation.restaurantvoting.util.ClockHolder;
import org.graduation.restaurantvoting.util.validation.ValidationUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractControllerTest {
    protected static final LocalDateTime BEFORE_EDIT_ENDTIME = LocalDateTime.of(
            LocalDate.now().getYear(),
            LocalDate.now().getMonth(),
            LocalDate.now().getDayOfMonth(),
            ValidationUtil.END_TIME_FOR_CHANGES.getHour() - 1,
            0, 0);
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUpClock() {
        final Clock fixed = Clock.fixed(BEFORE_EDIT_ENDTIME.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        ClockHolder.setClock(fixed);
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
