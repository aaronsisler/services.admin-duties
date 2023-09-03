package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseTables;
import com.ebsolutions.dal.dtos.EventDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Event;
import com.ebsolutions.models.MetricsStopWatch;
import com.ebsolutions.utils.UniqueIdGenerator;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Prototype
public class EventDao {

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<EventDto> eventTable;

    public EventDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.eventTable = this.enhancedClient.table(DatabaseTables.EVENT, TableSchema.fromBean(EventDto.class));
    }

    public Event read(String clientId, String eventId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(eventId).build();

            EventDto eventDto = eventTable.getItem(key);

            return eventDto == null
                    ? null
                    : Event.builder()
                    .clientId(eventDto.getClientId())
                    .eventId(eventDto.getEventId())
                    .locationId(eventDto.getLocationId())
                    .organizerId(eventDto.getOrganizerId())
                    .name(eventDto.getName())
                    .category(eventDto.getCategory())
                    .description(eventDto.getDescription())
                    .dayOfWeek(DayOfWeek.of(eventDto.getDayOfWeek()))
                    .startTime(eventDto.getStartTime())
                    .duration(eventDto.getDuration())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public List<Event> readAll(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).build();
            QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
            List<EventDto> eventDtos = eventTable.query(queryConditional).items().stream().collect(Collectors.toList());

            return eventDtos.stream()
                    .map(eventDto ->
                            Event.builder()
                                    .clientId(eventDto.getClientId())
                                    .eventId(eventDto.getEventId())
                                    .locationId(eventDto.getLocationId())
                                    .organizerId(eventDto.getOrganizerId())
                                    .name(eventDto.getName())
                                    .category(eventDto.getCategory())
                                    .description(eventDto.getDescription())
                                    .dayOfWeek(DayOfWeek.of(eventDto.getDayOfWeek()))
                                    .startTime(eventDto.getStartTime())
                                    .duration(eventDto.getDuration())
                                    .createdOn(eventDto.getCreatedOn())
                                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                                    .build()
                    ).collect(Collectors.toList());

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public void delete(String clientId, String eventId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(eventId).build();

            eventTable.deleteItem(key);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public Event create(Event event) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocalDateTime now = LocalDateTime.now();
            EventDto eventDto = EventDto.builder()
                    .clientId(event.getClientId())
                    .eventId(UniqueIdGenerator.generate())
                    .locationId(event.getLocationId())
                    .organizerId(event.getOrganizerId())
                    .name(event.getName())
                    .category(event.getCategory())
                    .description(event.getDescription())
                    .dayOfWeek(event.getDayOfWeek().getValue())
                    .startTime(event.getStartTime())
                    .duration(event.getDuration())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            eventTable.updateItem(eventDto);

            return Event.builder()
                    .clientId(eventDto.getClientId())
                    .eventId(eventDto.getEventId())
                    .locationId(eventDto.getLocationId())
                    .organizerId(eventDto.getOrganizerId())
                    .name(eventDto.getName())
                    .category(eventDto.getCategory())
                    .description(eventDto.getDescription())
                    .dayOfWeek(DayOfWeek.of(eventDto.getDayOfWeek()))
                    .startTime(eventDto.getStartTime())
                    .duration(eventDto.getDuration())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    /**
     * This will replace the entire database object with the input client
     *
     * @param event the object to replace the current database object
     */
    public void update(Event event) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            EventDto eventDto = EventDto.builder()
                    .clientId(event.getClientId())
                    .eventId(event.getEventId())
                    .locationId(event.getLocationId())
                    .organizerId(event.getOrganizerId())
                    .name(event.getName())
                    .category(event.getCategory())
                    .description(event.getDescription())
                    .dayOfWeek(event.getDayOfWeek().getValue())
                    .startTime(event.getStartTime())
                    .duration(event.getDuration())
                    .createdOn(event.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            eventTable.putItem(eventDto);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }
}
