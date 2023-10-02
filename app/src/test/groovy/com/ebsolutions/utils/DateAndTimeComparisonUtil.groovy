package com.ebsolutions.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class DateAndTimeComparisonUtil {
    static boolean areDateTimesEqual(LocalDateTime expectedDate, LocalDateTime testResult) {
        return expectedDate.until(testResult, ChronoUnit.SECONDS) == 0
    }

    /**
     * Check that dateTime is close to now (within 1 second for testing purposes)
     */
    static boolean isDateTimeNow(LocalDateTime testResult) {
        return testResult.until(LocalDateTime.now(), ChronoUnit.SECONDS) < 1
    }

    static boolean areDatesEqual(LocalDate expectedDate, LocalDate testResult) {
        return expectedDate.until(testResult, ChronoUnit.DAYS) == 0
    }

    static boolean areTimesEqual(LocalTime expectedTime, LocalTime testResult) {
        return expectedTime.until(testResult, ChronoUnit.SECONDS) == 0
    }
}
