package com.ebsolutions.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateComparisonUtil {
    public static boolean areDateTimesEqual(LocalDateTime firstDate, LocalDateTime secondDate) {
        return firstDate.until(secondDate, ChronoUnit.SECONDS) == 0;
    }

    /**
     * Check that dateTime is close to now (within 1 second for testing purposes)
     */
    public static boolean isDateTimeNow(LocalDateTime dateTime) {
        return dateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) < 1;
    }
}
