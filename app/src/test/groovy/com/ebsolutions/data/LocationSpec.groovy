package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.models.Location
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

import java.text.MessageFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@MicronautTest
class LocationSpec extends Specification {
    @Inject
    private HttpClient httpClient

    private String clientsUrl = TestConstants.adminServiceUrl + "/data/clients"

    def "Get a Location: Success"() {
        given: "A location exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to the location"
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    clientsUrl,
                    TestConstants.getLocationClientId,
                    TestConstants.getLocationId)

            HttpResponse<Location> response = httpClient.toBlocking()
                    .exchange(getUrl, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Location location = response.body()
            Assertions.assertEquals(TestConstants.getLocationClientId, location.getClientId())
            Assertions.assertEquals(TestConstants.getLocationId, location.getLocationId())
            Assertions.assertEquals("Get Mock Location", location.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(location.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(location.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
    }

    def "Get a Location: Fails given Location does not exist"() {
        given: "A location does not exist in the database"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the location"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations/non-existent-location",
                    clientsUrl,
                    TestConstants.getLocationClientId)

            HttpResponse<Location> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Create a Location: Fails given client ids do not match"() {
        given: "A valid location"
            Location createLocation = Location.builder()
                    .clientId(TestConstants.createLocationClientId)
                    .name("New Mock Location")
                    .build()

        when: "a request is made to create a location for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations",
                    clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.POST(incorrectUrl, createLocation)
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Create a Location: Success"() {
        given: "A valid location"
            Location createLocation = Location.builder()
                    .clientId(TestConstants.createLocationClientId)
                    .name("New Mock Location")
                    .build()

        when: "a request is made to create a location for the correct client"
            String correctUrl = MessageFormat.format("{0}/{1}/locations",
                    clientsUrl,
                    TestConstants.createLocationClientId)

            HttpRequest httpRequest = HttpRequest.POST(correctUrl, createLocation)
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Location location = response.body()
            Assertions.assertEquals(TestConstants.createLocationClientId, location.getClientId())
            Assertions.assertNotNull(location.getLocationId())
            Assertions.assertEquals("New Mock Location", location.getName())
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(location.getCreatedOn()))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(location.getLastUpdatedOn()))
    }

    def "Update a Location: Fails given client ids do not match"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    clientsUrl,
                    TestConstants.updateLocationClientId,
                    TestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(TestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(TestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location", initLocation.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the client id that is valid"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            updatedLocation.setName("New Updated Mock Location")

        when: "a request is made to update a location for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations",
                    clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(incorrectUrl), updatedLocation)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location: Fails given invalid Location Id"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    clientsUrl,
                    TestConstants.updateLocationClientId,
                    TestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(TestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(TestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location", initLocation.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the location id that is invalid"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            updatedLocation.setLocationId("")

        when: "a request is made to update the location"
            String updateUrl = MessageFormat.format("{0}/{1}/locations",
                    clientsUrl,
                    TestConstants.updateLocationClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedLocation)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location: Fail given create date is after now"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    clientsUrl,
                    TestConstants.updateLocationClientId,
                    TestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(TestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(TestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location", initLocation.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the created on date that is invalid"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            // Add an extra day to "now" since that is what the controller tests
            updatedLocation.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))

        when: "a request is made to update the location"
            String updateUrl = MessageFormat.format("{0}/{1}/locations",
                    clientsUrl,
                    TestConstants.updateLocationClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedLocation)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location: Success"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String correctUrl =
                    MessageFormat.format("{0}/{1}/locations/{2}",
                            clientsUrl,
                            TestConstants.updateLocationClientId,
                            TestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(correctUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(TestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(TestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location", initLocation.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to location"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            updatedLocation.setName("New Updated Mock Location")

        when: "a request is made to update the location"
            String updateUrl = MessageFormat.format("{0}/{1}/locations",
                    clientsUrl,
                    TestConstants.updateLocationClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedLocation)
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the updated location is returned"
            Location location = response.body()
            Assertions.assertEquals(TestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(TestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("New Updated Mock Location", location.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(location.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(location.getLastUpdatedOn()))
    }

    def "Delete a Location"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl =
                    MessageFormat.format("{0}/{1}/locations/{2}",
                            clientsUrl,
                            TestConstants.deleteLocationClientId,
                            TestConstants.deleteLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(TestConstants.deleteLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(TestConstants.deleteLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Delete Mock Location", initLocation.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initLocation.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        when: "a request is made to delete the location"
            String deleteUrl =
                    MessageFormat.format("{0}/{1}/locations/{2}",
                            clientsUrl,
                            TestConstants.deleteLocationClientId,
                            TestConstants.deleteLocationId)

            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(deleteUrl))
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the location no longer exists in the database"
            HttpResponse<Location> verifyDeletionResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, verifyDeletionResponse.code())
    }
}