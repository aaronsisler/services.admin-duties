package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.ClientDao;
import com.ebsolutions.dal.dtos.ClientDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Client;
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
@Controller("/data/client")
public class ClientController {
    private final ClientDao clientDao;

    public ClientController(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Post(value = "/")
    public HttpResponse<?> postClient(@Valid @Body Client client) {
        try {
            return ok(clientDao.create(client));
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Put(value = "/")
    public HttpResponse<?> updateClient(@Valid @Body Client client) {
        try {
            if (StringValidator.isBlank(client.getClientId())
                    || !LocalDateValidator.isBeforeNow(client.getCreatedOn())
            ) {
                return badRequest();
            }
            clientDao.update(client);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Get(value = "/{clientId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getClient(@NotBlank @PathVariable String clientId) {
        try {
            ClientDto clientDto = clientDao.read(clientId);

            return clientDto != null ? ok(clientDto) : noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }

    @Delete(value = "/{clientId}")
    public HttpResponse<?> deleteClient(@NotBlank @PathVariable String clientId) {
        try {
            clientDao.delete(clientId);

            return noContent();
        } catch (DataProcessingException dbe) {
            return serverError(dbe);
        }
    }
}
