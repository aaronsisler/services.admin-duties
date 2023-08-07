package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.EventDao;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Event;
import com.ebsolutions.models.RequestMethod;
import com.ebsolutions.validators.LocalDateValidator;
import com.ebsolutions.validators.RequestValidator;
import com.ebsolutions.validators.StringValidator;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

import static io.micronaut.http.HttpResponse.*;

@Slf4j
@Controller("/data/clients/{clientId}/events")
public class EventController {
    private final EventDao eventDao;

    public EventController(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Post(value = "/")
    public HttpResponse<?> postEvent(@NotBlank @PathVariable String clientId, @Valid @Body Event event) {
        try {
            if (!clientId.matches(event.getClientId())
                    || !RequestValidator.isEventValid(RequestMethod.POST, event)) {
                return badRequest();
            }

            return ok(eventDao.create(event));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put(value = "/")
    public HttpResponse<?> putEvent(@NotBlank @PathVariable String clientId, @Valid @Body Event event) {
        try {
            if (!clientId.matches(event.getClientId())
                    || StringValidator.isBlank(event.getEventId())
                    || !LocalDateValidator.isBeforeNow(event.getCreatedOn())
            ) {
                return badRequest();
            }
            eventDao.update(event);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(value = "/{eventId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getEvent(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String eventId) {
        try {
            Event event = eventDao.read(clientId, eventId);

            return event != null ? ok(event) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Delete(value = "/{eventId}")
    public HttpResponse<?> deleteEvent(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String eventId) {
        try {
            eventDao.delete(clientId, eventId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
