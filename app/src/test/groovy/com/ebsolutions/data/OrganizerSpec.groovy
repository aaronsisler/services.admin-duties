package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.models.Organizer
import com.ebsolutions.utils.CopyObjectUtil
import com.ebsolutions.utils.DateComparisonUtil
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
                    TestConstants.getOrganizerClientId,
                    TestConstants.getOrganizerId)

            HttpResponse<Organizer> response = httpClient.toBlocking()
                    .exchange(getUrl, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct organizer is returned"
            Organizer organizer = response.body()
            Assertions.assertEquals(TestConstants.getOrganizerClientId, organizer.getClientId())
            Assertions.assertEquals(TestConstants.getOrganizerId, organizer.getOrganizerId())
            Assertions.assertEquals("Get Mock Organizer", organizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(organizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(organizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
    }

    def "Get an Organizer: An organizer does not exist"() {
        given: "An organizer does not exist in the database"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the organizer"
            String incorrectUrl = MessageFormat.format("{0}/{1}/organizers/non-existent-organizer",
                    TestConstants.clientsUrl,
                    TestConstants.getOrganizerClientId)

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
                    TestConstants.getAllOrganizerClientId)
            HttpRequest httpRequest = HttpRequest.GET(getUrl)

            HttpResponse<List<Organizer>> response = httpClient.toBlocking()
                    .exchange(httpRequest, Argument.listOf(Organizer.class))

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct organizers are returned"
            List<Organizer> organizers = response.body()
            Organizer firstOrganizer = organizers.get(0)
            Organizer secondOrganizer = organizers.get(1)

            Assertions.assertEquals(TestConstants.getAllOrganizerClientId, firstOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.getAllOrganizerIdOne, firstOrganizer.getOrganizerId())
            Assertions.assertEquals("Get All Mock Organizer 1", firstOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(firstOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(firstOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

            Assertions.assertEquals(TestConstants.getAllOrganizerClientId, secondOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.getAllOrganizerIdTwo, secondOrganizer.getOrganizerId())
            Assertions.assertEquals("Get All Mock Organizer 2", secondOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(secondOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(secondOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
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
                    .clientId(TestConstants.createOrganizerClientId)
                    .name("New Mock Organizer")
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
                    .clientId(TestConstants.createOrganizerClientId)
                    .name("New Mock Organizer")
                    .build()

        when: "a request is made to create an organizer for the correct client"
            String correctUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.createOrganizerClientId)

            HttpRequest httpRequest = HttpRequest.POST(correctUrl, createOrganizer)
            HttpResponse<Organizer> response = httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct organizer is returned"
            Organizer organizer = response.body()
            Assertions.assertEquals(TestConstants.createOrganizerClientId, organizer.getClientId())
            Assertions.assertNotNull(organizer.getOrganizerId())
            Assertions.assertEquals("New Mock Organizer", organizer.getName())
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(organizer.getCreatedOn()))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(organizer.getLastUpdatedOn()))
    }

    def "Update an Organizer: Fails given client ids do not match"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/organizers/{2}",
                    TestConstants.clientsUrl,
                    TestConstants.updateOrganizerClientId,
                    TestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(TestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer", initOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

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
                    TestConstants.updateOrganizerClientId,
                    TestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(TestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer", initOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the organizer id that is invalid"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            updatedOrganizer.setOrganizerId("")

        when: "a request is made to update the organizer"
            String updateUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.updateOrganizerClientId)

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
                    TestConstants.updateOrganizerClientId,
                    TestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(TestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer", initOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the created on date that is invalid"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            // Add an extra day to "now" since that is what the controller tests
            updatedOrganizer.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))

        when: "a request is made to update the organizer"
            String updateUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.updateOrganizerClientId)

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
                            TestConstants.updateOrganizerClientId,
                            TestConstants.updateOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(correctUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(TestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Update Mock Organizer", initOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to organizer"
            Organizer updatedOrganizer = CopyObjectUtil.organizer(initOrganizer)
            updatedOrganizer.setName("New Updated Mock Organizer")

        when: "a request is made to update the organizer"
            String updateUrl = MessageFormat.format("{0}/{1}/organizers",
                    TestConstants.clientsUrl,
                    TestConstants.updateOrganizerClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedOrganizer)
            HttpResponse<Organizer> response = httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the updated organizer is returned"
            Organizer organizer = response.body()
            Assertions.assertEquals(TestConstants.updateOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.updateOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("New Updated Mock Organizer", organizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(organizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(organizer.getLastUpdatedOn()))
    }

    def "Delete an Organizer"() {
        given: "An organizer exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl =
                    MessageFormat.format("{0}/{1}/organizers/{2}",
                            TestConstants.clientsUrl,
                            TestConstants.deleteOrganizerClientId,
                            TestConstants.deleteOrganizerId)

            HttpResponse<Organizer> initResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Organizer initOrganizer = initResponse.body()
            Assertions.assertEquals(TestConstants.deleteOrganizerClientId, initOrganizer.getClientId())
            Assertions.assertEquals(TestConstants.deleteOrganizerId, initOrganizer.getOrganizerId())
            Assertions.assertEquals("Delete Mock Organizer", initOrganizer.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initOrganizer.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        when: "a request is made to delete the organizer"
            String deleteUrl =
                    MessageFormat.format("{0}/{1}/organizers/{2}",
                            TestConstants.clientsUrl,
                            TestConstants.deleteOrganizerClientId,
                            TestConstants.deleteOrganizerId)

            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(deleteUrl))
            HttpResponse<Organizer> response = httpClient.toBlocking().exchange(httpRequest, Organizer)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the organizer no longer exists in the database"
            HttpResponse<Organizer> verifyDeletionResponse = httpClient.toBlocking().exchange(getUrl, Organizer)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, verifyDeletionResponse.code())
    }
}