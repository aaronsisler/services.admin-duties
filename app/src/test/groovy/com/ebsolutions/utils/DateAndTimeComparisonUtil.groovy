package com.ebsolutions.utils

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class DateAndTimeComparisonUtil {
    static boolean areDateTimesEqual(LocalDateTime firstDate, LocalDateTime secondDate) {
        return firstDate.until(secondDate, ChronoUnit.SECONDS) == 0
    }

    /**
     * Check that dateTime is close to now (within 1 second for testing purposes)
     */
    static boolean isDateTimeNow(LocalDateTime dateTime) {
        return dateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) < 1
    }

    static boolean areTimesEqual(String firstTime, LocalTime secondTime) {
        return LocalTime.parse(firstTime).until(secondTime, ChronoUnit.SECONDS) == 0
    }
}
