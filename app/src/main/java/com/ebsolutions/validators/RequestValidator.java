package com.ebsolutions.validators;

import com.ebsolutions.models.*;

import java.time.LocalDate;

public class RequestValidator {
    public static boolean isEventValid(RequestMethod requestMethod, Event event) {
        return switch (requestMethod) {
            case POST -> RequestValidator.isPostBaseEventValid(event);
            case GET, PUT, DELETE -> true;
        };
    }

    public static boolean isWorkshopValid(RequestMethod requestMethod, Workshop workshop) {
        return switch (requestMethod) {
            case POST -> RequestValidator.isPostBaseEventValid(workshop);
            case GET, PUT, DELETE -> true;
        };
    }

    private static boolean isPostBaseEventValid(BaseEvent baseEvent) {
        return baseEvent.getDuration() > 0;
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
