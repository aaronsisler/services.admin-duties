package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseConstants;
import com.ebsolutions.dal.SortKeyType;
import com.ebsolutions.dal.dtos.EventDto;
import com.ebsolutions.dal.utils.KeyBuilder;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Event;
import com.ebsolutions.models.MetricsStopWatch;
import com.ebsolutions.utils.UniqueIdGenerator;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.sortBeginsWith;

@Slf4j
@Prototype
public class EventDao {
    private final DynamoDbTable<EventDto> ddbTable;

    public EventDao(DynamoDbEnhancedClient enhancedClient) {
        this.ddbTable = enhancedClient.table(DatabaseConstants.DATABASE_TABLE_NAME, TableSchema.fromBean(EventDto.class));
    }

    public Event read(String clientId, String eventId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.EVENT, eventId);

            EventDto eventDto = ddbTable.getItem(key);

            return eventDto == null
                    ? null
                    : Event.builder()
                    .clientId(eventDto.getPartitionKey())
                    .eventId(StringUtils.remove(eventDto.getSortKey(), SortKeyType.EVENT.name()))
                    .locationId(StringUtils.remove(eventDto.getLocationId(), SortKeyType.LOCATION.name()))
                    .organizerId(StringUtils.remove(eventDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                    .name(eventDto.getName())
                    .category(eventDto.getCategory())
                    .description(eventDto.getDescription())
                    .dayOfWeek(DayOfWeek.of(eventDto.getDayOfWeek()))
                    .startTime(eventDto.getStartTime())
                    .duration(eventDto.getDuration())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public List<Event> readAll(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            List<EventDto> eventDtos = ddbTable
                    .query(r -> r.queryConditional(
                            sortBeginsWith(s
                                    -> s.partitionValue(clientId).sortValue(SortKeyType.EVENT.name()).build()))
                    )
                    .items()
                    .stream()
                    .toList();

            return eventDtos.stream()
                    .map(eventDto ->
                            Event.builder()
                                    .clientId(eventDto.getPartitionKey())
                                    .eventId(StringUtils.remove(eventDto.getSortKey(), SortKeyType.EVENT.name()))
                                    .locationId(StringUtils.remove(eventDto.getLocationId(), SortKeyType.LOCATION.name()))
                                    .organizerId(StringUtils.remove(eventDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
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

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public void delete(String clientId, String eventId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.EVENT, eventId);

            ddbTable.deleteItem(key);

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public Event create(Event event) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocalDateTime now = LocalDateTime.now();
            EventDto eventDto = EventDto.builder()
                    .partitionKey(event.getClientId())
                    .sortKey(SortKeyType.EVENT.name() + UniqueIdGenerator.generate())
                    .locationId(SortKeyType.LOCATION.name() + event.getLocationId())
                    .organizerId(SortKeyType.ORGANIZER.name() + event.getOrganizerId())
                    .name(event.getName())
                    .category(event.getCategory())
                    .description(event.getDescription())
                    .dayOfWeek(event.getDayOfWeek().getValue())
                    .startTime(event.getStartTime())
                    .duration(event.getDuration())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            ddbTable.updateItem(eventDto);

            return Event.builder()
                    .clientId(eventDto.getPartitionKey())
                    .eventId(StringUtils.remove(eventDto.getSortKey(), SortKeyType.EVENT.name()))
                    .locationId(StringUtils.remove(eventDto.getLocationId(), SortKeyType.LOCATION.name()))
                    .organizerId(StringUtils.remove(eventDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                    .name(eventDto.getName())
                    .category(eventDto.getCategory())
                    .description(eventDto.getDescription())
                    .dayOfWeek(DayOfWeek.of(eventDto.getDayOfWeek()))
                    .startTime(eventDto.getStartTime())
                    .duration(eventDto.getDuration())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    /**
     * This will replace the entire database object with the input client
     *
     * @param event the object to replace the current database object
     */
    public Event update(Event event) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            EventDto eventDto = EventDto.builder()
                    .partitionKey(event.getClientId())
                    .sortKey(SortKeyType.EVENT.name() + event.getEventId())
                    .locationId(SortKeyType.LOCATION.name() + event.getLocationId())
                    .organizerId(SortKeyType.ORGANIZER.name() + event.getOrganizerId())
                    .name(event.getName())
                    .category(event.getCategory())
                    .description(event.getDescription())
                    .dayOfWeek(event.getDayOfWeek().getValue())
                    .startTime(event.getStartTime())
                    .duration(event.getDuration())
                    .createdOn(event.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            ddbTable.putItem(eventDto);

            return Event.builder()
                    .clientId(eventDto.getPartitionKey())
                    .eventId(StringUtils.remove(eventDto.getSortKey(), SortKeyType.EVENT.name()))
                    .locationId(StringUtils.remove(eventDto.getLocationId(), SortKeyType.LOCATION.name()))
                    .organizerId(StringUtils.remove(eventDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                    .name(eventDto.getName())
                    .category(eventDto.getCategory())
                    .description(eventDto.getDescription())
                    .dayOfWeek(DayOfWeek.of(eventDto.getDayOfWeek()))
                    .startTime(eventDto.getStartTime())
                    .duration(eventDto.getDuration())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }
}
