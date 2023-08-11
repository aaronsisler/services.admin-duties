package com.ebsolutions.validators;

import com.ebsolutions.models.CsvRequest;
import com.ebsolutions.models.Event;
import com.ebsolutions.models.RequestMethod;

import java.time.LocalDate;

public class RequestValidator {
    public static boolean isEventValid(RequestMethod requestMethod, Event event) {
        return switch (requestMethod) {
            case POST -> RequestValidator.isPostEventValid(event);
            case GET, PUT, DELETE -> true;
        };
    }

    private static boolean isPostEventValid(Event event) {
        return event.getDuration() > 0;
    }

    public static boolean isCsvRequestValid(CsvRequest csvRequest) {
        boolean isYearValid = csvRequest.getYear() >= LocalDate.now().getYear();
        boolean isMonthValid = (1 <= csvRequest.getMonth() && csvRequest.getMonth() <= 12);

        if (!isYearValid || !isMonthValid) {
            return false;
        }

        return true;
    }
}
