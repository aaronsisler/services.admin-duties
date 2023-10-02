package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.LocationDao;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Location;
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
@Controller("/data/clients/{clientId}/locations")
public class LocationController {
    private final LocationDao locationDao;

    public LocationController(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Get(value = "/{locationId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> get(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String locationId) {
        try {
            Location location = locationDao.read(clientId, locationId);

            return location != null ? ok(location) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getAll(@NotBlank @PathVariable String clientId) {
        try {
            List<Location> locations = locationDao.readAll(clientId);

            return !locations.isEmpty() ? ok(locations) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Post()
    public HttpResponse<?> post(@NotBlank @PathVariable String clientId, @Valid @Body Location location) {
        try {
            if (!clientId.matches(location.getClientId())) {
                return badRequest();
            }

            return ok(locationDao.create(location));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put()
    public HttpResponse<?> put(@NotBlank @PathVariable String clientId, @Valid @Body Location location) {
        try {
            if (!clientId.matches(location.getClientId())
                    || StringValidator.isBlank(location.getLocationId())
                    || !LocalDateValidator.isBeforeNow(location.getCreatedOn())
            ) {
                return badRequest();
            }

            return ok(locationDao.update(location));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Delete(value = "/{locationId}")
    public HttpResponse<?> delete(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String locationId) {
        try {
            locationDao.delete(clientId, locationId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
