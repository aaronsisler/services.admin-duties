package com.ebsolutions.controllers.data;

import com.ebsolutions.dal.daos.ClientDao;
import com.ebsolutions.dal.dtos.ClientDto;
import com.ebsolutions.exceptions.DataRetrievalException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.extern.slf4j.Slf4j;

import static io.micronaut.http.HttpResponse.*;

@Slf4j
@Controller("/data/client")
public class ClientController {
    private final ClientDao clientDao;

    public ClientController(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Get(value = "/{clientId}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getClient(@PathVariable String clientId) {
        try {
            ClientDto clientDto = clientDao.get(clientId);

            return clientDto != null ? ok(clientDto) : noContent();
        } catch (DataRetrievalException dbe) {
            return serverError(dbe);
        }
    }
}
