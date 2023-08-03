package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.LocationDao;
import com.ebsolutions.dal.dtos.LocationDto;
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

import static io.micronaut.http.HttpResponse.*;

@Slf4j
@Controller("/data/client/{clientId}/locations")
public class LocationController {
    private final LocationDao locationDao;

    public LocationController(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Post(value = "/")
    public HttpResponse<?> postLocation(@NotBlank @PathVariable String clientId, @Valid @Body Location location) {
        try {
            if (!clientId.matches(location.getClientId())) {
                return badRequest();
            }
            ;
            return ok(locationDao.create(location));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put(value = "/")
    public HttpResponse<?> putLocation(@NotBlank @PathVariable String clientId, @Valid @Body Location location) {
        try {
            if (!clientId.matches(location.getClientId())
                    || StringValidator.isBlank(location.getLocationId())
                    || !LocalDateValidator.isBeforeNow(location.getCreatedOn())
            ) {
                return badRequest();
            }
            locationDao.update(location);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(value = "/{locationId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getLocation(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String locationId) {
        try {
            LocationDto locationDto = locationDao.read(clientId, locationId);

            return locationDto != null ? ok(locationDto) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Delete(value = "/{locationId}")
    public HttpResponse<?> deleteLocation(@NotBlank @PathVariable String clientId, @NotBlank @PathVariable String locationId) {
        try {
            locationDao.delete(clientId, locationId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
