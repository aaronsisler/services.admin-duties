package com.ebsolutions.data

import com.ebsolutions.models.Client
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import spock.lang.Specification

@MicronautTest
class ClientSpec extends Specification {
    @Inject
    private HttpClient client;

    private String clientsUrl = "http://localhost:8080/data/clients";
    private String clientId = "body-kinect-wellness";

    def "Get a Client"() {
        given: "A client exists in the database"
            // Need to seed data here
        when: "a request is made to the client endpoint"
            HttpResponse<Client> response = client.toBlocking().exchange(clientsUrl + "/" + clientId, Client);

        then: "the correct status code is returned"
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_OK, response.code());

        and: "the correct body is returned"
            Client client = response.body();

            Assertions.assertEquals(clientId, client.getClientId());
            Assertions.assertEquals("Body Kinect Wellness", client.getName());
    }
}