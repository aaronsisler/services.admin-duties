package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.OrganizerDao;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Organizer;
import com.ebsolutions.validators.LocalDateValidator;
import com.ebsolutions.validators.StringValidator;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

import static io.micronaut.http.HttpResponse.*;

@Slf4j
@Controller("/data/clients/{clientId}/organizers")
public class OrganizerController {
    private final OrganizerDao organizerDao;

    public OrganizerController(OrganizerDao organizerDao) {
        this.organizerDao = organizerDao;
    }

    @Post(value = "/")
    public HttpResponse<?> postClient(@NotBlank @PathVariable String clientId, @Valid @Body Organizer organizer) {
        try {
            if (!clientId.matches(organizer.getClientId())) {
                return badRequest();
            }

            return ok(organizerDao.create(organizer));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put(value = "/")
    public HttpResponse<?> putOrganizer(@NotBlank @PathVariable String clientId, @Valid @Body Organizer organizer) {
        try {
            if (!clientId.matches(organizer.getClientId())
                    || StringValidator.isBlank(organizer.getOrganizerId())
                    || !LocalDateValidator.isBeforeNow(organizer.getCreatedOn())
            ) {
                return badRequest();
            }
            organizerDao.update(organizer);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(value = "/{organizerId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getOrganizer(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String organizerId) {
        try {
            Organizer organizer = organizerDao.read(clientId, organizerId);

            return organizer != null ? ok(organizer) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Delete(value = "/{organizerId}")
    public HttpResponse<?> deleteOrganizer(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String organizerId) {
        try {
            organizerDao.delete(clientId, organizerId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
