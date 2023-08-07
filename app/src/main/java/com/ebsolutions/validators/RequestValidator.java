package com.ebsolutions.validators;

import com.ebsolutions.models.Event;
import com.ebsolutions.models.RequestMethod;

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
}
