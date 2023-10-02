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

import java.util.List;

import static io.micronaut.http.HttpResponse.*;

@Slf4j
@Controller("/data/clients/{clientId}/organizers")
public class OrganizerController {
    private final OrganizerDao organizerDao;

    public OrganizerController(OrganizerDao organizerDao) {
        this.organizerDao = organizerDao;
    }

    @Get(value = "/{organizerId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> get(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String organizerId) {
        try {
            Organizer organizer = organizerDao.read(clientId, organizerId);

            return organizer != null ? ok(organizer) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getAll(@NotBlank @PathVariable String clientId) {
        try {
            List<Organizer> organizers = organizerDao.readAll(clientId);

            return !organizers.isEmpty() ? ok(organizers) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Post()
    public HttpResponse<?> post(@NotBlank @PathVariable String clientId, @Valid @Body Organizer organizer) {
        try {
            if (!clientId.matches(organizer.getClientId())) {
                return badRequest();
            }

            return ok(organizerDao.create(organizer));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put()
    public HttpResponse<?> put(@NotBlank @PathVariable String clientId, @Valid @Body Organizer organizer) {
        try {
            if (!clientId.matches(organizer.getClientId())
                    || StringValidator.isBlank(organizer.getOrganizerId())
                    || !LocalDateValidator.isBeforeNow(organizer.getCreatedOn())
            ) {
                return badRequest();
            }
            return ok(organizerDao.update(organizer));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }


    @Delete(value = "/{organizerId}")
    public HttpResponse<?> delete(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String organizerId) {
        try {
            organizerDao.delete(clientId, organizerId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
