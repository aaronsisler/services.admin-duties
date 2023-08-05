package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseTables;
import com.ebsolutions.dal.dtos.EventDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Event;
import com.ebsolutions.utils.UniqueIdGenerator;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.LocalDateTime;

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
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(eventId).build();

            EventDto eventDto = eventTable.getItem(key);

            return eventDto == null
                    ? null
                    : Event.builder()
                    .clientId(eventDto.getClientId())
                    .eventId(eventDto.getEventId())
                    .name(eventDto.getName())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        }
    }

    public void delete(String clientId, String eventId) {
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(eventId).build();

            eventTable.deleteItem(key);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        }
    }

    public Event create(Event event) {
        try {
            LocalDateTime now = LocalDateTime.now();
            EventDto eventDto = EventDto.builder()
                    .clientId(event.getClientId())
                    .eventId(UniqueIdGenerator.generate())
                    .name(event.getName())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            eventTable.updateItem(eventDto);

            return Event.builder()
                    .clientId(eventDto.getClientId())
                    .eventId(eventDto.getEventId())
                    .name(eventDto.getName())
                    .createdOn(eventDto.getCreatedOn())
                    .lastUpdatedOn(eventDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        }
    }

    /**
     * This will replace the entire database object with the input client
     *
     * @param event the object to replace the current database object
     */
    public void update(Event event) {
        try {
            EventDto eventDto = EventDto.builder()
                    .clientId(event.getClientId())
                    .eventId(event.getEventId())
                    .name(event.getName())
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
        }
    }
}
