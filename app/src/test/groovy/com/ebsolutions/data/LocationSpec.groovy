package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.constants.LocationTestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.models.Location
import com.ebsolutions.utils.CopyObjectUtil
import com.ebsolutions.utils.DateAndTimeComparisonUtil
import io.micronaut.core.type.Argument
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

    def "Get a Location: Given location exists"() {
        given: "A location exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to the location"
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    TestConstants.clientsUrl,
                    LocationTestConstants.getLocationClientId,
                    LocationTestConstants.getLocationId)

            HttpResponse<Location> response = httpClient.toBlocking()
                    .exchange(getUrl, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Location location = response.body()
            Assertions.assertEquals(LocationTestConstants.getLocationClientId, location.getClientId())
            Assertions.assertEquals(LocationTestConstants.getLocationId, location.getLocationId())
            Assertions.assertEquals("Get Mock Location Name", location.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(location.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(location.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
    }

    def "Get a Location: Given Location does not exist"() {
        given: "A location does not exist in the database"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the location"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations/non-existent-location",
                    TestConstants.clientsUrl,
                    LocationTestConstants.getLocationClientId)

            HttpResponse<Location> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Get all Locations: Locations exist for client"() {
        given: "A set of locations exist in the database for a given client"
            // Data seeded from Database init scripts
        when: "a request is made to retrieve the locations"
            String getUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    LocationTestConstants.getAllLocationClientId)
            HttpRequest httpRequest = HttpRequest.GET(getUrl)

            HttpResponse<List<Location>> response = httpClient.toBlocking()
                    .exchange(httpRequest, Argument.listOf(Location.class))

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct locations are returned"
            List<Location> locations = response.body()
            Location firstLocation = locations.get(0)
            Location secondLocation = locations.get(1)

            Assertions.assertEquals(LocationTestConstants.getAllLocationClientId, firstLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.getAllLocationIdOne, firstLocation.getLocationId())
            Assertions.assertEquals("Get All Mock Location Name 1", firstLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, firstLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, firstLocation.getLastUpdatedOn()))

            Assertions.assertEquals(LocationTestConstants.getAllLocationClientId, secondLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.getAllLocationIdTwo, secondLocation.getLocationId())
            Assertions.assertEquals("Get All Mock Location Name 2", secondLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, secondLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, secondLocation.getLastUpdatedOn()))
    }

    def "Get all Locations: No locations exist for client"() {
        given: "No locations exist in the database for a given client"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the locations"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpResponse<Location> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Delete a Location"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl =
                    MessageFormat.format("{0}/{1}/locations/{2}",
                            TestConstants.clientsUrl,
                            LocationTestConstants.deleteLocationClientId,
                            LocationTestConstants.deleteLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(LocationTestConstants.deleteLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.deleteLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Delete Mock Location Name", initLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initLocation.getLastUpdatedOn()))
        when: "a request is made to delete the location"
            String deleteUrl =
                    MessageFormat.format("{0}/{1}/locations/{2}",
                            TestConstants.clientsUrl,
                            LocationTestConstants.deleteLocationClientId,
                            LocationTestConstants.deleteLocationId)

            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(deleteUrl))
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the location no longer exists in the database"
            HttpResponse<Location> verifyDeletionResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, verifyDeletionResponse.code())
    }

    def "Create a Location: Fails given client ids do not match"() {
        given: "A valid location"
            Location createLocation = Location.builder()
                    .clientId(LocationTestConstants.createLocationClientId)
                    .name("New Mock Location")
                    .build()

        when: "a request is made to create a location for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.POST(incorrectUrl, createLocation)
            httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Create a Location: Success"() {
        given: "A valid location"
            Location createLocation = Location.builder()
                    .clientId(LocationTestConstants.createLocationClientId)
                    .name("Create Mock Location Name")
                    .build()

        when: "a request is made to create a location for the correct client"
            String correctUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    LocationTestConstants.createLocationClientId)

            HttpRequest httpRequest = HttpRequest.POST(correctUrl, createLocation)
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct location is returned"
            Location location = response.body()
            Assertions.assertEquals(LocationTestConstants.createLocationClientId, location.getClientId())
            Assertions.assertNotNull(location.getLocationId())
            Assertions.assertEquals("Create Mock Location Name", location.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(location.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(location.getLastUpdatedOn()))
    }

    def "Update a Location: Fails given client ids do not match"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    TestConstants.clientsUrl,
                    LocationTestConstants.updateLocationClientId,
                    LocationTestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(LocationTestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location Name", initLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initLocation.getLastUpdatedOn()))

        and: "an update is made to the client id that is valid"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            updatedLocation.setName("New Updated Mock Location")

        when: "a request is made to update a location for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
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
                    TestConstants.clientsUrl,
                    LocationTestConstants.updateLocationClientId,
                    LocationTestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(LocationTestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location Name", initLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initLocation.getLastUpdatedOn()))

        and: "an update is made to the location id that is invalid"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            updatedLocation.setLocationId("")

        when: "a request is made to update the location"
            String updateUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    LocationTestConstants.updateLocationClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedLocation)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update a Location: Fails given create date is after now"() {
        given: "A location exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/locations/{2}",
                    TestConstants.clientsUrl,
                    LocationTestConstants.updateLocationClientId,
                    LocationTestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(getUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(LocationTestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location Name", initLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initLocation.getLastUpdatedOn()))

        and: "an update is made to the created on date that is invalid"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            // Add an extra day to "now" since that is what the controller tests
            updatedLocation.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))

        when: "a request is made to update the location"
            String updateUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    LocationTestConstants.updateLocationClientId)

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
                            TestConstants.clientsUrl,
                            LocationTestConstants.updateLocationClientId,
                            LocationTestConstants.updateLocationId)

            HttpResponse<Location> initResponse = httpClient.toBlocking().exchange(correctUrl, Location)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Location initLocation = initResponse.body()
            Assertions.assertEquals(LocationTestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("Update Mock Location Name", initLocation.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initLocation.getLastUpdatedOn()))

        and: "an update is made to location"
            Location updatedLocation = CopyObjectUtil.location(initLocation)
            updatedLocation.setName("New Updated Mock Location Name")

        when: "a request is made to update the location"
            String updateUrl = MessageFormat.format("{0}/{1}/locations",
                    TestConstants.clientsUrl,
                    LocationTestConstants.updateLocationClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedLocation)
            HttpResponse<Location> response = httpClient.toBlocking().exchange(httpRequest, Location)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the updated location is returned"
            Location location = response.body()
            Assertions.assertEquals(LocationTestConstants.updateLocationClientId, initLocation.getClientId())
            Assertions.assertEquals(LocationTestConstants.updateLocationId, initLocation.getLocationId())
            Assertions.assertEquals("New Updated Mock Location Name", location.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initLocation.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(location.getLastUpdatedOn()))
    }


}