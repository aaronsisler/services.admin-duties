package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.constants.OrganizerTestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.models.Organizer
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
class OrganizerSpec extends Specification {
    @Inject
    private HttpClient httpClient

    def "Get an Organizer: An organizer exists"() {
        given: "An organizer exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to the organizer"
            String getUrl = MessageFormat.format("{0}/{1}/organizers/{2}",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.getOrganizerClientId,
                    OrganizerTestConstants.getOrganizerId)

            HttpResponse<Organizer> response = httpClient.toBlocking()
                    .exchange(getUrl, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct organizer is returned"
            Organizer organizer = response.body()
            Assertions.assertEquals(OrganizerTestConstants.getOrganizerClientId, organizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.getOrganizerId, organizer.getOrganizerId())
            Assertions.assertEquals("Get Mock Organizer Name", organizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, organizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, organizer.getLastUpdatedOn()))
    }

    def "Get an Organizer: An organizer does not exist"() {
        given: "An organizer does not exist in the database"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the organizer"
            String incorrectUrl = MessageFormat.format("{0}/{1}/organizers/non-existent-organizer",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.getOrganizerClientId)

            HttpResponse<Organizer> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Get all Organizers: Organizers exist for client"() {
        given: "A set of organizers exist in the database for a given client"
            // Data seeded from Database init scripts
        when: "a request is made to retrieve the organizers"
            String getUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.getAllOrganizerClientId)
            HttpRequest httpRequest = HttpRequest.GET(getUrl)

            HttpResponse<List<Organizer>> response = httpClient.toBlocking()
                    .exchange(httpRequest, Argument.listOf(Organizer.class))

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct organizers are returned"
            List<Organizer> organizers = response.body()
            Organizer firstOrganizer = organizers.get(0)
            Organizer secondOrganizer = organizers.get(1)

            Assertions.assertEquals(OrganizerTestConstants.getAllOrganizerClientId, firstOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.getAllOrganizerIdOne, firstOrganizer.getOrganizerId())
            Assertions.assertEquals("Get All Mock Organizer Name 1", firstOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, firstOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, firstOrganizer.getLastUpdatedOn()))

            Assertions.assertEquals(OrganizerTestConstants.getAllOrganizerClientId, secondOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.getAllOrganizerIdTwo, secondOrganizer.getOrganizerId())
            Assertions.assertEquals("Get All Mock Organizer Name 2", secondOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, secondOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, secondOrganizer.getLastUpdatedOn()))
    }

    def "Get all Organizers: No organizers exist for client"() {
        given: "No organizers exist in the database for a given client"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the organizers"
            String incorrectUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpResponse<Organizer> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Create an Organizer: Fails given client ids do not match"() {
        given: "A valid organizer"
            Organizer createOrganizer = Organizer.builder()
                    .clientId(OrganizerTestConstants.createOrganizerClientId)
                    .name("Create Mock Organizer Name")
                    .build()

        when: "a request is made to create an organizer for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.POST(incorrectUrl, createOrganizer)
            httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Create an Organizer: Success"() {
        given: "A valid organizer"
            Organizer createOrganizer = Organizer.builder()
                    .clientId(OrganizerTestConstants.createOrganizerClientId)
                    .name("Create Mock Organizer Name")
                    .build()

        when: "a request is made to create an organizer for the correct client"
            String correctUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.createOrganizerClientId)

            HttpRequest httpRequest = HttpRequest.POST(correctUrl, createOrganizer)
            HttpResponse<Organizer> response = httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct organizer is returned"
            Organizer organizer = response.body()
            Assertions.assertEquals(OrganizerTestConstants.createOrganizerClientId, organizer.getClientId())
            Assertions.assertNotNull(organizer.getOrganizerId())
            Assertions.assertEquals("Create Mock Organizer Name", organizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(organizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(organizer.getLastUpdatedOn()))
    }

    def "Update an Organizer: Fails given client ids do not match"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/organizers/{2}",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.updateOrganizerClientId,
                    OrganizerTestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer Name", initOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initOrganizer.getLastUpdatedOn()))

        and: "an update is made to the client id that is valid"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            updatedOrganizer.setName("New Updated Mock Organizer")

        when: "a request is made to update an organizer for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(incorrectUrl), updatedOrganizer)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Organizer: Fails given invalid Organizer Id"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/organizers/{2}",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.updateOrganizerClientId,
                    OrganizerTestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer Name", initOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initOrganizer.getLastUpdatedOn()))

        and: "an update is made to the organizer id that is invalid"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            updatedOrganizer.setOrganizerId("")

        when: "a request is made to update the organizer"
            String updateUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.updateOrganizerClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedOrganizer)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Organizer: Fails given create date is after now"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/organizers/{2}",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.updateOrganizerClientId,
                    OrganizerTestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer Name", initOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initOrganizer.getLastUpdatedOn()))

        and: "an update is made to the created on date that is invalid"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            // Add an extra day to "now" since that is what the controller tests
            updatedOrganizer.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))

        when: "a request is made to update the organizer"
            String updateUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.updateOrganizerClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedOrganizer)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Organizer: Success"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String correctUrl =
                    MessageFormat.format("{0}/{1}/organizers/{2}",
                            TestConstants.clientsUrl,
                            OrganizerTestConstants.updateOrganizerClientId,
                            OrganizerTestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(correctUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer Name", initOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initOrganizer.getLastUpdatedOn()))

        and: "an update is made to organizer"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            updatedOrganizer.setName("New Updated Mock Organizer Name")

        when: "a request is made to update the organizer"
            String updateUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    OrganizerTestConstants.updateOrganizerClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedOrganizer)
            HttpResponse<Organizer> response = httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the updated organizer is returned"
            Organizer organizer = response.body()
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("New Updated Mock Organizer Name", organizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, organizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(organizer.getLastUpdatedOn()))
    }

    def "Delete an Organizer"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl =
                    MessageFormat.format("{0}/{1}/organizers/{2}",
                            TestConstants.clientsUrl,
                            OrganizerTestConstants.deleteOrganizerClientId,
                            OrganizerTestConstants.deleteOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(OrganizerTestConstants.deleteOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(OrganizerTestConstants.deleteOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Delete Mock Organizer Name", initOrganizer.getName())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initOrganizer.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initOrganizer.getLastUpdatedOn()))

        when: "a request is made to delete the organizer"
            String deleteUrl =
                    MessageFormat.format("{0}/{1}/organizers/{2}",
                            TestConstants.clientsUrl,
                            OrganizerTestConstants.deleteOrganizerClientId,
                            OrganizerTestConstants.deleteOrganizerId)

            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(deleteUrl))
            HttpResponse<Organizer> response = httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the organizer no longer exists in the database"
            HttpResponse<Organizer> verifyDeletionResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, verifyDeletionResponse.code())
    }
}