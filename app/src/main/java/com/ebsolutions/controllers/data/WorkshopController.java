package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.WorkshopDao;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.RequestMethod;
import com.ebsolutions.models.Workshop;
import com.ebsolutions.validators.LocalDateValidator;
import com.ebsolutions.validators.RequestValidator;
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
@Controller("/data/clients/{clientId}/workshops")
public class WorkshopController {
    private final WorkshopDao workshopDao;

    public WorkshopController(WorkshopDao workshopDao) {
        this.workshopDao = workshopDao;
    }

    @Get(value = "/{workshopId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> get(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String workshopId) {
        try {
            Workshop workshop = workshopDao.read(clientId, workshopId);

            return workshop != null ? ok(workshop) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getAll(@NotBlank @PathVariable String clientId) {
        try {
            List<Workshop> workshops = workshopDao.readAll(clientId);

            return !workshops.isEmpty() ? ok(workshops) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Post()
    public HttpResponse<?> post(@NotBlank @PathVariable String clientId, @Valid @Body Workshop workshop) {
        try {
            if (!clientId.matches(workshop.getClientId())
                    || !RequestValidator.isWorkshopValid(RequestMethod.POST, workshop)) {
                return badRequest();
            }

            return ok(workshopDao.create(workshop));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put()
    public HttpResponse<?> put(@NotBlank @PathVariable String clientId, @Valid @Body Workshop workshop) {
        try {
            if (!clientId.matches(workshop.getClientId())
                    || StringValidator.isBlank(workshop.getWorkshopId())
                    || !LocalDateValidator.isBeforeNow(workshop.getCreatedOn())
            ) {
                return badRequest();
            }
            return ok(workshopDao.update(workshop));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }


    @Delete(value = "/{workshopId}")
    public HttpResponse<?> delete(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String workshopId) {
        try {
            workshopDao.delete(clientId, workshopId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
