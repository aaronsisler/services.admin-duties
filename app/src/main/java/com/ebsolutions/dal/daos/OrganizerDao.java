package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseConstants;
import com.ebsolutions.dal.dtos.OrganizerDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.MetricsStopWatch;
import com.ebsolutions.models.Organizer;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Prototype
public class OrganizerDao {

    private final DynamoDbTable<OrganizerDto> ddbTable;

    public OrganizerDao(DynamoDbEnhancedClient enhancedClient) {
        this.ddbTable = enhancedClient.table(DatabaseConstants.DATABASE_TABLE_NAME, TableSchema.fromBean(OrganizerDto.class));
    }

    public Organizer read(String clientId, String organizerId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(organizerId).build();

            OrganizerDto organizerDto = ddbTable.getItem(key);

            return organizerDto == null
                    ? null
                    : Organizer.builder()
                    .clientId(organizerDto.getPartitionKey())
                    .organizerId(organizerDto.getSortKey())
                    .name(organizerDto.getName())
                    .createdOn(organizerDto.getCreatedOn())
                    .lastUpdatedOn(organizerDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public List<Organizer> readAll(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).build();
            QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
            List<OrganizerDto> organizerDtos = ddbTable.query(queryConditional).items().stream().toList();

            return organizerDtos.stream()
                    .map(organizerDto ->
                            Organizer.builder()
                                    .clientId(organizerDto.getPartitionKey())
                                    .organizerId(organizerDto.getSortKey())
                                    .name(organizerDto.getName())
                                    .createdOn(organizerDto.getCreatedOn())
                                    .lastUpdatedOn(organizerDto.getLastUpdatedOn())
                                    .build()
                    ).collect(Collectors.toList());

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "readAll"));
        }
    }

    public void delete(String clientId, String organizerId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();

        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(organizerId).build();

            ddbTable.deleteItem(key);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "delete"));
        }
    }

    public Organizer create(Organizer organizer) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();

        try {
            LocalDateTime now = LocalDateTime.now();
            OrganizerDto organizerDto = OrganizerDto.builder()
                    .partitionKey(organizer.getClientId())
                    .sortKey(UniqueIdGenerator.generate())
                    .name(organizer.getName())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            ddbTable.updateItem(organizerDto);

            return Organizer.builder()
                    .clientId(organizerDto.getPartitionKey())
                    .organizerId(organizerDto.getSortKey())
                    .name(organizerDto.getName())
                    .createdOn(organizerDto.getCreatedOn())
                    .lastUpdatedOn(organizerDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "create"));
        }
    }

    /**
     * This will replace the entire database object with the input client
     *
     * @param organizer the object to replace the current database object
     */
    public void update(Organizer organizer) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();

        try {
            OrganizerDto organizerDto = OrganizerDto.builder()
                    .partitionKey(organizer.getClientId())
                    .sortKey(organizer.getOrganizerId())
                    .name(organizer.getName())
                    .createdOn(organizer.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            ddbTable.putItem(organizerDto);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "update"));
        }
    }
}
