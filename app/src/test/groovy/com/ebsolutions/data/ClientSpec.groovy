package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.models.Client
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import spock.lang.Specification

import java.time.temporal.ChronoUnit

@MicronautTest
class ClientSpec extends Specification {
    @Inject
    private HttpClient client;

    private String clientsUrl = TestConstants.adminServiceUrl + "/data/clients";

    def "Get a Client"() {
        given: "A client exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to the client endpoint"
            HttpResponse<Client> response = client.toBlocking().exchange(clientsUrl + "/" + TestConstants.clientId, Client);

        then: "the correct status code is returned"
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_OK, response.code());

        and: "the correct body is returned"
            Client client = response.body();

            Assertions.assertEquals(TestConstants.clientId, client.getClientId());
            Assertions.assertEquals("Mock Client", client.getName());
            Assertions.assertEquals(client.getCreatedOn().until(TestConstants.createdOn, ChronoUnit.SECONDS), 0);
            Assertions.assertEquals(client.getLastUpdatedOn().until(TestConstants.lastUpdatedOn, ChronoUnit.SECONDS), 0);
    }
}