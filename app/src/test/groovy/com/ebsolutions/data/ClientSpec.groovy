package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.utils.DateComparisonUtil
import io.micronaut.http.HttpRequest
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

    private String clientsUrl = TestConstants.adminServiceUrl + "/data/clients";

    def "Get a Client"() {
        given: "A client exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to retrieve a single client"
            HttpResponse<Client> response = client.toBlocking().exchange(clientsUrl + "/" + TestConstants.clientId, Client);

        then: "the correct status code is returned"
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_OK, response.code());

        and: "the correct client is returned"
            Client client = response.body();

            Assertions.assertEquals(TestConstants.clientId, client.getClientId());
            Assertions.assertEquals("Mock Client", client.getName());
            Assertions.assertTrue(DateComparisonUtil.areDatesEqual(client.getCreatedOn(), TestConstants.createdOn));
            Assertions.assertTrue(DateComparisonUtil.areDatesEqual(client.getLastUpdatedOn(), TestConstants.lastUpdatedOn));
    }

    def "Delete a Client"() {
        given: "A client exists in the database"
            // Verify data seeded from Database init scripts correctly
            HttpResponse<Client> initResponse = client.toBlocking().exchange(clientsUrl + "/" + TestConstants.deleteClientId, Client);
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_OK, initResponse.code());

            Client initClient = initResponse.body();
            Assertions.assertEquals(TestConstants.deleteClientId, initClient.getClientId());
            Assertions.assertEquals("Delete Mock Client", initClient.getName());
            Assertions.assertTrue(DateComparisonUtil.areDatesEqual(initClient.getCreatedOn(), TestConstants.createdOn));
            Assertions.assertTrue(DateComparisonUtil.areDatesEqual(initClient.getLastUpdatedOn(), TestConstants.lastUpdatedOn));
        when: "a request is made to delete the client"
            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(clientsUrl + "/" + TestConstants.deleteClientId));
            HttpResponse<Client> response = client.toBlocking().exchange(httpRequest, Client);

        then: "the correct status code is returned"
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_NO_CONTENT, response.code());

        and: "the client no longer exists in the database"
            HttpResponse<Client> postResponse = client.toBlocking().exchange(clientsUrl + "/" + TestConstants.deleteClientId, Client);
            Assertions.assertEquals(java.net.HttpURLConnection.HTTP_NO_CONTENT, postResponse.code());
    }
}