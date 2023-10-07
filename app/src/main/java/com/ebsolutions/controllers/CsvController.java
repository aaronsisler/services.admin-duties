package com.ebsolutions.controllers;

import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.CsvRequest;
import com.ebsolutions.models.CsvResponse;
import com.ebsolutions.services.OrchestrationService;
import com.ebsolutions.utils.UniqueIdGenerator;
import com.ebsolutions.validators.RequestValidator;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;

import java.io.IOException;

import static io.micronaut.http.HttpResponse.*;

@Controller("/csv")
public class CsvController {
    private final OrchestrationService orchestrationService;

    public CsvController(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    @Post()
    public HttpResponse<?> postCsv(@Valid @Body CsvRequest csvRequest) {
        try {
            if (!RequestValidator.isCsvRequestValid(csvRequest)) {
                return badRequest();
            }

            String trackingId = UniqueIdGenerator.generate();
            csvRequest.setTrackingId(trackingId);

            this.orchestrationService.createCsv(csvRequest);

            return accepted().body(CsvResponse.builder().trackingId(trackingId).build());
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        } catch (IOException ioe) {
            return serverError(ioe);
        }
    }
}
