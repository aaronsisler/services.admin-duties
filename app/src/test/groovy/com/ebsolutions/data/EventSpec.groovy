package com.ebsolutions.data

import com.ebsolutions.config.TestConstants
import com.ebsolutions.models.Client
import com.ebsolutions.models.Event
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
class EventSpec extends Specification {
    @Inject
    private HttpClient httpClient

    def "Get an Event: An event exists"() {
        given: "An event exists in the database"
            // Data seeded from Database init scripts
        when: "a request is made to the event"
            String getUrl = MessageFormat.format("{0}/{1}/events/{2}",
                    TestConstants.clientsUrl,
                    TestConstants.getEventClientId,
                    TestConstants.getEventId)

            HttpResponse<Event> response = httpClient.toBlocking()
                    .exchange(getUrl, Event)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct event is returned"
            Event event = response.body()
            Assertions.assertEquals(TestConstants.getEventClientId, event.getClientId())
            Assertions.assertEquals(TestConstants.getEventId, event.getEventId())
            Assertions.assertEquals("Get Mock Event", event.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(event.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(event.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
    }

    def "Get an Event: An event does not exist"() {
        given: "An event does not exist in the database"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the event"
            String incorrectUrl = MessageFormat.format("{0}/{1}/events/non-existent-event",
                    TestConstants.clientsUrl,
                    TestConstants.getEventClientId)

            HttpResponse<Event> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Event)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Get all Events: Events exist for client"() {
        given: "A set of events exist in the database for a given client"
            // Data seeded from Database init scripts
        when: "a request is made to retrieve the events"
            String getUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.getAllEventClientId)
            HttpRequest httpRequest = HttpRequest.GET(getUrl)

            HttpResponse<List<Event>> response = httpClient.toBlocking()
                    .exchange(httpRequest, Argument.listOf(Event.class))

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct events are returned"
            List<Event> events = response.body()
            Event firstEvent = events.get(0)
            Event secondEvent = events.get(1)

            Assertions.assertEquals(TestConstants.getAllEventClientId, firstEvent.getClientId())
            Assertions.assertEquals(TestConstants.getAllEventIdOne, firstEvent.getEventId())
            Assertions.assertEquals("Get All Mock Event 1", firstEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(firstEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(firstEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

            Assertions.assertEquals(TestConstants.getAllEventClientId, secondEvent.getClientId())
            Assertions.assertEquals(TestConstants.getAllEventIdTwo, secondEvent.getEventId())
            Assertions.assertEquals("Get All Mock Event 2", secondEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(secondEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(secondEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
    }

    def "Get all Events: No events exist for client"() {
        given: "No events exist in the database for a given client"
            // No data seeded from Database init scripts
        when: "a request is made to retrieve the events"
            String incorrectUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpResponse<Event> response = httpClient.toBlocking()
                    .exchange(incorrectUrl, Event)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())
    }

    def "Create an Event: Fails given client ids do not match"() {
        given: "A valid event"
            Event createEvent = Event.builder()
                    .clientId(TestConstants.createEventClientId)
                    .name("New Mock Event")
                    .build()

        when: "a request is made to create an event for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.POST(incorrectUrl, createEvent)
            httpClient.toBlocking().exchange(httpRequest, Event)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Create an Event: Success"() {
        given: "A valid event"
            Event createEvent = Event.builder()
                    .clientId(TestConstants.createEventClientId)
                    .name("New Mock Event")
                    .build()

        when: "a request is made to create an event for the correct client"
            String correctUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.createEventClientId)

            HttpRequest httpRequest = HttpRequest.POST(correctUrl, createEvent)
            HttpResponse<Event> response = httpClient.toBlocking().exchange(httpRequest, Event)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the correct event is returned"
            Event event = response.body()
            Assertions.assertEquals(TestConstants.createEventClientId, event.getClientId())
            Assertions.assertNotNull(event.getEventId())
            Assertions.assertEquals("New Mock Event", event.getName())
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(event.getCreatedOn()))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(event.getLastUpdatedOn()))
    }

    def "Update an Event: Fails given client ids do not match"() {
        given: "An event exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/events/{2}",
                    TestConstants.clientsUrl,
                    TestConstants.updateEventClientId,
                    TestConstants.updateEventId)

            HttpResponse<Event> initResponse = httpClient.toBlocking().exchange(getUrl, Event)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Event initEvent = initResponse.body()
            Assertions.assertEquals(TestConstants.updateEventClientId, initEvent.getClientId())
            Assertions.assertEquals(TestConstants.updateEventId, initEvent.getEventId())
            Assertions.assertEquals("Update Mock Event", initEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the client id that is valid"
            Event updatedEvent = CopyObjectUtil.event(initEvent)
            updatedEvent.setName("New Updated Mock Event")

        when: "a request is made to update an event for the wrong client"
            String incorrectUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.nonExistentClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(incorrectUrl), updatedEvent)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Event: Fails given invalid Event Id"() {
        given: "An event exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/events/{2}",
                    TestConstants.clientsUrl,
                    TestConstants.updateEventClientId,
                    TestConstants.updateEventId)

            HttpResponse<Event> initResponse = httpClient.toBlocking().exchange(getUrl, Event)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Event initEvent = initResponse.body()
            Assertions.assertEquals(TestConstants.updateEventClientId, initEvent.getClientId())
            Assertions.assertEquals(TestConstants.updateEventId, initEvent.getEventId())
            Assertions.assertEquals("Update Mock Event", initEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the event id that is invalid"
            Event updatedEvent = CopyObjectUtil.event(initEvent)
            updatedEvent.setEventId("")

        when: "a request is made to update the event"
            String updateUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.updateEventClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedEvent)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Event: Fails given create date is after now"() {
        given: "An event exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl = MessageFormat.format("{0}/{1}/events/{2}",
                    TestConstants.clientsUrl,
                    TestConstants.updateEventClientId,
                    TestConstants.updateEventId)

            HttpResponse<Event> initResponse = httpClient.toBlocking().exchange(getUrl, Event)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Event initEvent = initResponse.body()
            Assertions.assertEquals(TestConstants.updateEventClientId, initEvent.getClientId())
            Assertions.assertEquals(TestConstants.updateEventId, initEvent.getEventId())
            Assertions.assertEquals("Update Mock Event", initEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to the created on date that is invalid"
            Event updatedEvent = CopyObjectUtil.event(initEvent)
            // Add an extra day to "now" since that is what the controller tests
            updatedEvent.setCreatedOn(LocalDateTime.now().plus(1, ChronoUnit.DAYS))

        when: "a request is made to update the event"
            String updateUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.updateEventClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedEvent)
            httpClient.toBlocking().exchange(httpRequest, Client)

        then: "the correct status code is returned"
            HttpClientResponseException ex = thrown()
            ex.status == HttpStatus.BAD_REQUEST
    }

    def "Update an Event: Success"() {
        given: "An event exists in the database"
            // Verify data seeded from Database init scripts correctly
            String correctUrl =
                    MessageFormat.format("{0}/{1}/events/{2}",
                            TestConstants.clientsUrl,
                            TestConstants.updateEventClientId,
                            TestConstants.updateEventId)

            HttpResponse<Event> initResponse = httpClient.toBlocking().exchange(correctUrl, Event)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Event initEvent = initResponse.body()
            Assertions.assertEquals(TestConstants.updateEventClientId, initEvent.getClientId())
            Assertions.assertEquals(TestConstants.updateEventId, initEvent.getEventId())
            Assertions.assertEquals("Update Mock Event", initEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))

        and: "an update is made to event"
            Event updatedEvent = CopyObjectUtil.event(initEvent)
            updatedEvent.setName("New Updated Mock Event")

        when: "a request is made to update the event"
            String updateUrl = MessageFormat.format("{0}/{1}/events",
                    TestConstants.clientsUrl,
                    TestConstants.updateEventClientId)

            HttpRequest httpRequest = HttpRequest.PUT(URI.create(updateUrl), updatedEvent)
            HttpResponse<Event> response = httpClient.toBlocking().exchange(httpRequest, Event)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, response.code())

        and: "the updated event is returned"
            Event event = response.body()
            Assertions.assertEquals(TestConstants.updateEventClientId, initEvent.getClientId())
            Assertions.assertEquals(TestConstants.updateEventId, initEvent.getEventId())
            Assertions.assertEquals("New Updated Mock Event", event.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(event.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.isDateTimeNow(event.getLastUpdatedOn()))
    }

    def "Delete an Event"() {
        given: "An event exists in the database"
            // Verify data seeded from Database init scripts correctly
            String getUrl =
                    MessageFormat.format("{0}/{1}/events/{2}",
                            TestConstants.clientsUrl,
                            TestConstants.deleteEventClientId,
                            TestConstants.deleteEventId)

            HttpResponse<Event> initResponse = httpClient.toBlocking().exchange(getUrl, Event)
            Assertions.assertEquals(HttpURLConnection.HTTP_OK, initResponse.code())

            Event initEvent = initResponse.body()
            Assertions.assertEquals(TestConstants.deleteEventClientId, initEvent.getClientId())
            Assertions.assertEquals(TestConstants.deleteEventId, initEvent.getEventId())
            Assertions.assertEquals("Delete Mock Event", initEvent.getName())
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getCreatedOn(), TestConstants.createdOn))
            Assertions.assertTrue(DateComparisonUtil.areDateTimesEqual(initEvent.getLastUpdatedOn(), TestConstants.lastUpdatedOn))
        when: "a request is made to delete the event"
            String deleteUrl =
                    MessageFormat.format("{0}/{1}/events/{2}",
                            TestConstants.clientsUrl,
                            TestConstants.deleteEventClientId,
                            TestConstants.deleteEventId)

            HttpRequest httpRequest = HttpRequest.DELETE(URI.create(deleteUrl))
            HttpResponse<Event> response = httpClient.toBlocking().exchange(httpRequest, Event)

        then: "the correct status code is returned"
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, response.code())

        and: "the event no longer exists in the database"
            HttpResponse<Event> verifyDeletionResponse = httpClient.toBlocking().exchange(getUrl, Event)
            Assertions.assertEquals(HttpURLConnection.HTTP_NO_CONTENT, verifyDeletionResponse.code())
    }
}