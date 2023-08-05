package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseTables;
import com.ebsolutions.dal.dtos.OrganizerDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Organizer;
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
public class OrganizerDao {

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<OrganizerDto> organizerTable;

    public OrganizerDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.organizerTable = this.enhancedClient.table(DatabaseTables.ORGANIZER, TableSchema.fromBean(OrganizerDto.class));
    }

    public Organizer read(String clientId, String organizerId) {
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(organizerId).build();

            OrganizerDto organizerDto = organizerTable.getItem(key);

            return organizerDto == null
                    ? null
                    : Organizer.builder()
                    .clientId(organizerDto.getClientId())
                    .organizerId(organizerDto.getOrganizerId())
                    .name(organizerDto.getName())
                    .createdOn(organizerDto.getCreatedOn())
                    .lastUpdatedOn(organizerDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        }
    }

    public void delete(String clientId, String organizerId) {
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(organizerId).build();

            organizerTable.deleteItem(key);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        }
    }

    public Organizer create(Organizer organizer) {
        try {
            LocalDateTime now = LocalDateTime.now();
            OrganizerDto organizerDto = OrganizerDto.builder()
                    .clientId(organizer.getClientId())
                    .organizerId(UniqueIdGenerator.generate())
                    .name(organizer.getName())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            organizerTable.updateItem(organizerDto);

            return Organizer.builder()
                    .clientId(organizerDto.getClientId())
                    .organizerId(organizerDto.getOrganizerId())
                    .name(organizerDto.getName())
                    .createdOn(organizerDto.getCreatedOn())
                    .lastUpdatedOn(organizerDto.getLastUpdatedOn())
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
     * @param organizer the object to replace the current database object
     */
    public void update(Organizer organizer) {
        try {
            OrganizerDto organizerDto = OrganizerDto.builder()
                    .clientId(organizer.getClientId())
                    .organizerId(organizer.getOrganizerId())
                    .name(organizer.getName())
                    .createdOn(organizer.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            organizerTable.putItem(organizerDto);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        }
    }
}
