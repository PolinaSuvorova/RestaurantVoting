package org.graduation.restaurantvoting.util;

import java.time.LocalDate;

public class DateTimeUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "hh:mm";

    // DB doesn't support LocalDate.MIN/MAX
    private static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    private static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);

    private DateTimeUtil() {
    }

    public static LocalDate setLocDateOrMin(LocalDate localDate) {
        return localDate != null ? localDate : MIN_DATE;
    }

    public static LocalDate setLocDateOrMax(LocalDate localDate) {
        return localDate != null ? localDate : MAX_DATE;
    }
}
