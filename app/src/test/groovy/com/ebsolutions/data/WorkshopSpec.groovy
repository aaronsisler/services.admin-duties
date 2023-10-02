package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.constants.WorkshopTestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.models.Workshop
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
class WorkshopSpec extends Specification {
    @Inject
    private HttpClient httpClient

    def "Get an Workshop: An workshop exists"() {
        given: "An workshop exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to the workshop"
            String getUrl = MessageFormat.format("{0}/{1}/workshops/{2}",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.getWorkshopClientId,
                    WorkshopTestConstants.getWorkshopId)

            HttpResponse<Workshop> response = httpClient.toBlocking()
                    .exchange(getUrl, Workshop)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct workshop is returned"
            Workshop workshop = response.body()
            Assertions.assertEquals(WorkshopTestConstants.getWorkshopClientId, workshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.getWorkshopId, workshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.getWorkshopLocationId, workshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.getWorkshopOrganizerId, workshop.getOrganizerId())
            Assertions.assertEquals("Get Mock Workshop Name", workshop.getName())
            Assertions.assertEquals("Get Mock Workshop Category", workshop.getCategory())
            Assertions.assertEquals("Get Mock Workshop Description", workshop.getDescription())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.getWorkshopWorkshopDate, workshop.getWorkshopDate()))
            Assertions.assertEquals(WorkshopTestConstants.getWorkshopCost, workshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.getWorkshopDuration, workshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.getWorkshopStartTime, workshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, workshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, workshop.getLastUpdatedOn()))
    }

    def "Get an Workshop: An workshop does not exist"() {
        given: "An workshop does not exist in the database"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the workshop"
            String incorrectUrl = MessageFormat.format("{0}/{1}/workshops/non-existent-workshop",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.getWorkshopClientId)

            HttpResponse<Workshop> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Workshop)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Get all Workshops: Workshops exist for client"() {
        given: "A set of workshops exist in the database for a given client"
            // Data seeded from Database init scripts
        when: "a request is made to retrieve the workshops"
            String getUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.getAllWorkshopClientId)
            HttpRequest httpRequest = HttpRequest.GET(getUrl)

            HttpResponse<List<Workshop>> response = httpClient.toBlocking()
                    .exchange(httpRequest, Argument.listOf(Workshop.class))

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct workshops are returned"
            List<Workshop> workshops = response.body()
            Workshop firstWorkshop = workshops.get(0)
            Workshop secondWorkshop = workshops.get(1)

            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopClientId, firstWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopIdOne, firstWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopLocationIdOne, firstWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopOrganizerIdOne, firstWorkshop.getOrganizerId())
            Assertions.assertEquals("Get All Mock Workshop Name 1", firstWorkshop.getName())
            Assertions.assertEquals("Get All Mock Workshop Category 1", firstWorkshop.getCategory())
            Assertions.assertEquals("Get All Mock Workshop Description 1", firstWorkshop.getDescription())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.getAllWorkshopWorkshopDateOne, firstWorkshop.getWorkshopDate()))
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopCostOne, firstWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopDurationOne, firstWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.getAllWorkshopStartTimeOne, firstWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, firstWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, firstWorkshop.getLastUpdatedOn()))

            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopClientId, secondWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopIdTwo, secondWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopLocationIdTwo, secondWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopOrganizerIdTwo, secondWorkshop.getOrganizerId())
            Assertions.assertEquals("Get All Mock Workshop Name 2", secondWorkshop.getName())
            Assertions.assertEquals("Get All Mock Workshop Category 2", secondWorkshop.getCategory())
            Assertions.assertEquals("Get All Mock Workshop Description 2", secondWorkshop.getDescription())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.getAllWorkshopWorkshopDateTwo, secondWorkshop.getWorkshopDate()))
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopCostTwo, secondWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.getAllWorkshopDurationTwo, secondWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.getAllWorkshopStartTimeTwo, secondWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, secondWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, secondWorkshop.getLastUpdatedOn()))
    }

    def "Get all Workshops: No workshops exist for client"() {
        given: "No workshops exist in the database for a given client"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the workshops"
            String incorrectUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpResponse<Workshop> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Workshop)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Delete an Workshop"() {
        given: "An workshop exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl =
                    MessageFormat.format("{0}/{1}/workshops/{2}",
                            TestConstants.clientsUrl,
                            WorkshopTestConstants.deleteWorkshopClientId,
                            WorkshopTestConstants.deleteWorkshopId)

            HttpResponse<Workshop> initResponse = httpClient.toBlocking().exchange(getUrl, Workshop)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Workshop initWorkshop = initResponse.body()
            Assertions.assertEquals(WorkshopTestConstants.deleteWorkshopClientId, initWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.deleteWorkshopId, initWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.deleteWorkshopLocationId, initWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.deleteWorkshopOrganizerId, initWorkshop.getOrganizerId())
            Assertions.assertEquals("Delete Mock Workshop Name", initWorkshop.getName())
            Assertions.assertEquals("Delete Mock Workshop Category", initWorkshop.getCategory())
            Assertions.assertEquals("Delete Mock Workshop Description", initWorkshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.deleteWorkshopCost, initWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.deleteWorkshopDuration, initWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.deleteWorkshopWorkshopDate, initWorkshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.deleteWorkshopStartTime, initWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initWorkshop.getLastUpdatedOn()))

        when: "a request is made to delete the workshop"
            String deleteUrl =
                    MessageFormat.format("{0}/{1}/workshops/{2}",
                            TestConstants.clientsUrl,
                            WorkshopTestConstants.deleteWorkshopClientId,
                            WorkshopTestConstants.deleteWorkshopId)

            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(deleteUrl))
            HttpResponse<Workshop> response = httpClient.toBlocking().exchange(httpRequest, Workshop)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the workshop no longer exists in the database"
            HttpResponse<Workshop> verifyDeletionResponse = httpClient.toBlocking().exchange(getUrl, Workshop)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, verifyDeletionResponse.code())
    }

    def "Create an Workshop: Fails given client ids do not match"() {
        given: "A valid workshop"
            Workshop createWorkshop = Workshop.builder()
                    .clientId(WorkshopTestConstants.createWorkshopClientId)
                    .name("Create Mock Workshop")
                    .build()

        when: "a request is made to create an workshop for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.POST(incorrectUrl, createWorkshop)
            httpClient.toBlocking().exchange(httpRequest, Workshop)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Create an Workshop: Success"() {
        given: "A valid workshop"
            Workshop createWorkshop = Workshop.builder()
                    .clientId(WorkshopTestConstants.createWorkshopClientId)
                    .locationId(WorkshopTestConstants.createWorkshopLocationId)
                    .organizerId(WorkshopTestConstants.createWorkshopOrganizerId)
                    .name("Create Mock Workshop")
                    .description("Create Mock Workshop Description")
                    .category("Create Mock Workshop Category")
                    .workshopDate(WorkshopTestConstants.createWorkshopWorkshopDate)
                    .cost(WorkshopTestConstants.createWorkshopCost)
                    .duration(WorkshopTestConstants.createWorkshopDuration)
                    .startTime(WorkshopTestConstants.createWorkshopStartTime)
                    .build()

        when: "a request is made to create an workshop for the correct client"
            String correctUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.createWorkshopClientId)

            HttpRequest httpRequest = HttpRequest.POST(correctUrl, createWorkshop)
            HttpResponse<Workshop> response = httpClient.toBlocking().exchange(httpRequest, Workshop)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct workshop is returned"
            Workshop workshop = response.body()
            Assertions.assertEquals(WorkshopTestConstants.createWorkshopClientId, workshop.getClientId())
            Assertions.assertNotNull(workshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.createWorkshopLocationId, workshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.createWorkshopOrganizerId, workshop.getOrganizerId())
            Assertions.assertEquals("Create Mock Workshop", workshop.getName())
            Assertions.assertEquals("Create Mock Workshop Category", workshop.getCategory())
            Assertions.assertEquals("Create Mock Workshop Description", workshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopCost, workshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopDuration, workshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.updateWorkshopWorkshopDate, workshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.updateWorkshopStartTime, workshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(workshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(workshop.getLastUpdatedOn()))
    }

    def "Update an Workshop: Fails given client ids do not match"() {
        given: "An workshop exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/workshops/{2}",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.updateWorkshopClientId,
                    WorkshopTestConstants.updateWorkshopId)

            HttpResponse<Workshop> initResponse = httpClient.toBlocking().exchange(getUrl, Workshop)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Workshop initWorkshop = initResponse.body()
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopClientId, initWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopId, initWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopLocationId, initWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopOrganizerId, initWorkshop.getOrganizerId())
            Assertions.assertEquals("Update Mock Workshop Name", initWorkshop.getName())
            Assertions.assertEquals("Update Mock Workshop Category", initWorkshop.getCategory())
            Assertions.assertEquals("Update Mock Workshop Description", initWorkshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopCost, initWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopDuration, initWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.updateWorkshopWorkshopDate, initWorkshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.updateWorkshopStartTime, initWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initWorkshop.getLastUpdatedOn()))

        and: "an update is made to the client id that is valid"
            Workshop updatedWorkshop = CopyObjectUtil.workshop(initWorkshop)
            updatedWorkshop.setName("New Updated Mock Workshop")

        when: "a request is made to update an workshop for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(incorrectUrl), updatedWorkshop)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Workshop: Fails given invalid Workshop Id"() {
        given: "An workshop exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/workshops/{2}",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.updateWorkshopClientId,
                    WorkshopTestConstants.updateWorkshopId)

            HttpResponse<Workshop> initResponse = httpClient.toBlocking().exchange(getUrl, Workshop)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Workshop initWorkshop = initResponse.body()
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopClientId, initWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopId, initWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopLocationId, initWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopOrganizerId, initWorkshop.getOrganizerId())
            Assertions.assertEquals("Update Mock Workshop Name", initWorkshop.getName())
            Assertions.assertEquals("Update Mock Workshop Category", initWorkshop.getCategory())
            Assertions.assertEquals("Update Mock Workshop Description", initWorkshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopCost, initWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopDuration, initWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.updateWorkshopWorkshopDate, initWorkshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.updateWorkshopStartTime, initWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initWorkshop.getLastUpdatedOn()))

        and: "an update is made to the workshop id that is invalid"
            Workshop updatedWorkshop = CopyObjectUtil.workshop(initWorkshop)
            updatedWorkshop.setWorkshopId("")

        when: "a request is made to update the workshop"
            String updateUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.updateWorkshopClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedWorkshop)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Workshop: Fails given create date is after now"() {
        given: "An workshop exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/workshops/{2}",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.updateWorkshopClientId,
                    WorkshopTestConstants.updateWorkshopId)

            HttpResponse<Workshop> initResponse = httpClient.toBlocking().exchange(getUrl, Workshop)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Workshop initWorkshop = initResponse.body()
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopClientId, initWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopId, initWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopLocationId, initWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopOrganizerId, initWorkshop.getOrganizerId())
            Assertions.assertEquals("Update Mock Workshop Name", initWorkshop.getName())
            Assertions.assertEquals("Update Mock Workshop Category", initWorkshop.getCategory())
            Assertions.assertEquals("Update Mock Workshop Description", initWorkshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopCost, initWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopDuration, initWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.updateWorkshopWorkshopDate, initWorkshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.updateWorkshopStartTime, initWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initWorkshop.getLastUpdatedOn()))

        and: "an update is made to the created on date that is invalid"
            Workshop updatedWorkshop = CopyObjectUtil.workshop(initWorkshop)
            // Add an extra day to "now" since that is what the controller tests
            updatedWorkshop.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))

        when: "a request is made to update the workshop"
            String updateUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.updateWorkshopClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedWorkshop)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Workshop: Success"() {
        given: "An workshop exists in the database"
            // Verify data seeded from Database init scripts correctly
            String correctUrl =
                    MessageFormat.format("{0}/{1}/workshops/{2}",
                            TestConstants.clientsUrl,
                            WorkshopTestConstants.updateWorkshopClientId,
                            WorkshopTestConstants.updateWorkshopId)

            HttpResponse<Workshop> initResponse = httpClient.toBlocking().exchange(correctUrl, Workshop)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Workshop initWorkshop = initResponse.body()
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopClientId, initWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopId, initWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopLocationId, initWorkshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopOrganizerId, initWorkshop.getOrganizerId())
            Assertions.assertEquals("Update Mock Workshop Name", initWorkshop.getName())
            Assertions.assertEquals("Update Mock Workshop Category", initWorkshop.getCategory())
            Assertions.assertEquals("Update Mock Workshop Description", initWorkshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopCost, initWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopDuration, initWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.updateWorkshopWorkshopDate, initWorkshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.updateWorkshopStartTime, initWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, initWorkshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.lastUpdatedOn, initWorkshop.getLastUpdatedOn()))

        and: "an update is made to workshop"
            Workshop updatedWorkshop = CopyObjectUtil.workshop(initWorkshop)
            updatedWorkshop.setName("New Updated Mock Workshop Name")

        when: "a request is made to update the workshop"
            String updateUrl = MessageFormat.format("{0}/{1}/workshops",
                    TestConstants.clientsUrl,
                    WorkshopTestConstants.updateWorkshopClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedWorkshop)
            HttpResponse<Workshop> response = httpClient.toBlocking().exchange(httpRequest, Workshop)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the updated workshop is returned"
            Workshop workshop = response.body()
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopClientId, initWorkshop.getClientId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopId, initWorkshop.getWorkshopId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopLocationId, workshop.getLocationId())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopOrganizerId, workshop.getOrganizerId())
            Assertions.assertEquals("New Updated Mock Workshop Name", workshop.getName())
            Assertions.assertEquals("Update Mock Workshop Category", workshop.getCategory())
            Assertions.assertEquals("Update Mock Workshop Description", workshop.getDescription())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopCost, initWorkshop.getCost())
            Assertions.assertEquals(WorkshopTestConstants.updateWorkshopDuration, initWorkshop.getDuration())
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDatesEqual(WorkshopTestConstants.updateWorkshopWorkshopDate, initWorkshop.getWorkshopDate()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areTimesEqual(WorkshopTestConstants.updateWorkshopStartTime, initWorkshop.getStartTime()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.areDateTimesEqual(TestConstants.createdOn, workshop.getCreatedOn()))
            Assertions.assertTrue(DateAndTimeComparisonUtil.isDateTimeNow(workshop.getLastUpdatedOn()))
    }
}