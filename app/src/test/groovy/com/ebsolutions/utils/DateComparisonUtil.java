package com.ebsolutions.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateComparisonUtil {
    public static boolean areDatesEqual(LocalDateTime firstDate, LocalDateTime secondDate) {
        return firstDate.until(secondDate, ChronoUnit.SECONDS) == 0;
    }
}
