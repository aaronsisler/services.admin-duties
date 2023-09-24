package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.utils.CopyObjectUtil
import com.ebsolutions.utils.DateComparisonUtil
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@MicronautTest
class LocationSpec extends Specification {
    @Inject
    private HttpClient httpClient

    private String locationUrl = TestConstants.adminServiceUrl + "/data/locations"

    def "Get a Location"() {
        given: "A location exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to retrieve a single location"
            HttpResponse<Client> response = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.clientId, Client)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Client client = response.body()
            Assertions.assertEquals(TestConstants.clientId, client.getClientId())
            Assertions.assertEquals("Mock Client", client.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(client.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(client.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
    }

    def "Create a Location: Fails given client id does not match"() {
        given: "A client valid location"
            Client newClient = Client.builder().name("New Mock Client").build()
        when: "a request is made to create a location"
            HttpRequest httpRequest = HttpRequest.POST(URI.create(locationUrl), newClient)
            HttpResponse<Client> response = httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Client client = response.body()
            Assertions.assertNotNull(client.getClientId())
            Assertions.assertEquals("New Mock Client", client.getName())
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(client.getCreatedOn()))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(client.getLastUpdatedOn()))
    }

    def "Create a Location"() {
        given: "A client valid location"
            Client newClient = Client.builder().name("New Mock Client").build()
        when: "a request is made to create a location"
            HttpRequest httpRequest = HttpRequest.POST(URI.create(locationUrl), newClient)
            HttpResponse<Client> response = httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Client client = response.body()
            Assertions.assertNotNull(client.getClientId())
            Assertions.assertEquals("New Mock Client", client.getName())
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(client.getCreatedOn()))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(client.getLastUpdatedOn()))
    }

    def "Update a Location: Fails given client id does not match"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            HttpResponse<Client> initResponse = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.updateClientId, Client)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Client initClient = initResponse.body()
            Assertions.assertEquals(TestConstants.updateClientId, initClient.getClientId())
            Assertions.assertEquals("Update Mock Client", initClient.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        and: "an update is made to the client id that is invalid"
            Client updatedClient = CopyObjectUtil.client(initClient)
            updatedClient.setClientId("")
            updatedClient.setName("New Updated Mock Client")
        when: "a request is made to update the client"
            HttpRequest httpRequest = HttpRequest.PUT(URI.create(locationUrl), updatedClient)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location: Fails given blank Location Id"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            HttpResponse<Client> initResponse = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.updateClientId, Client)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Client initClient = initResponse.body()
            Assertions.assertEquals(TestConstants.updateClientId, initClient.getClientId())
            Assertions.assertEquals("Update Mock Client", initClient.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        and: "an update is made to the location id that is invalid"
            Client updatedClient = CopyObjectUtil.client(initClient)
            updatedClient.setClientId("")
            updatedClient.setName("New Updated Mock Client")
        when: "a request is made to update the location"
            HttpRequest httpRequest = HttpRequest.PUT(URI.create(locationUrl), updatedClient)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location: Fail given create date is after now"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            HttpResponse<Client> initResponse = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.updateClientId, Client)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Client initClient = initResponse.body()
            Assertions.assertEquals(TestConstants.updateClientId, initClient.getClientId())
            Assertions.assertEquals("Update Mock Client", initClient.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        and: "an update is made to the created on date that is invalid"
            Client updatedClient = CopyObjectUtil.client(initClient)
            // Add an extra day to "now" since that is what the controller tests
            updatedClient.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
        when: "a request is made to update the location"
            HttpRequest httpRequest = HttpRequest.PUT(URI.create(locationUrl), updatedClient)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            HttpResponse<Client> initResponse = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.updateClientId, Client)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Client initClient = initResponse.body()
            Assertions.assertEquals(TestConstants.updateClientId, initClient.getClientId())
            Assertions.assertEquals("Update Mock Client", initClient.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        and: "an update is made to location"
            Client updatedClient = CopyObjectUtil.client(initClient)
            updatedClient.setName("New Updated Mock Client")
        when: "a request is made to update the location"
            HttpRequest httpRequest = HttpRequest.PUT(URI.create(locationUrl), updatedClient)
            HttpResponse<Client> response = httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is updated"
            Client client = response.body()
            Assertions.assertNotNull(client.getClientId())
            Assertions.assertEquals("New Updated Mock Client", client.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(client.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(client.getLastUpdatedOn()))
    }

    def "Delete a Location"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            HttpResponse<Client> initResponse = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.deleteClientId, Client)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Client initClient = initResponse.body()
            Assertions.assertEquals(TestConstants.deleteClientId, initClient.getClientId())
            Assertions.assertEquals("Delete Mock Client", initClient.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initClient.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        when: "a request is made to delete the location"
            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(locationUrl + "/" + TestConstants.deleteClientId))
            HttpResponse<Client> response = httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the location no longer exists in the database"
            HttpResponse<Client> postResponse = httpClient.toBlocking().exchange(locationUrl + "/" + TestConstants.deleteClientId, Client)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, postResponse.code())
    }
}