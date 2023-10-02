package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseConstants;
import com.ebsolutions.dal.SortKeyType;
import com.ebsolutions.dal.dtos.WorkshopDto;
import com.ebsolutions.dal.utils.KeyBuilder;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.MetricsStopWatch;
import com.ebsolutions.models.Workshop;
import com.ebsolutions.utils.UniqueIdGenerator;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.sortBeginsWith;

@Slf4j
@Prototype
public class WorkshopDao {
    private final DynamoDbTable<WorkshopDto> ddbTable;

    public WorkshopDao(DynamoDbEnhancedClient enhancedClient) {
        this.ddbTable = enhancedClient.table(DatabaseConstants.DATABASE_TABLE_NAME, TableSchema.fromBean(WorkshopDto.class));
    }

    public Workshop read(String clientId, String workshopId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.WORKSHOP, workshopId);

            WorkshopDto workshopDto = ddbTable.getItem(key);

            return workshopDto == null
                    ? null
                    : Workshop.builder()
                    .clientId(workshopDto.getPartitionKey())
                    .workshopId(StringUtils.remove(workshopDto.getSortKey(), SortKeyType.WORKSHOP.name()))
                    .locationId(StringUtils.remove(workshopDto.getLocationId(), SortKeyType.LOCATION.name()))
                    .organizerId(StringUtils.remove(workshopDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                    .name(workshopDto.getName())
                    .category(workshopDto.getCategory())
                    .description(workshopDto.getDescription())
                    .workshopDate(workshopDto.getWorkshopDate())
                    .cost(workshopDto.getCost())
                    .startTime(workshopDto.getStartTime())
                    .duration(workshopDto.getDuration())
                    .createdOn(workshopDto.getCreatedOn())
                    .lastUpdatedOn(workshopDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public List<Workshop> readAll(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            List<WorkshopDto> workshopDtos = ddbTable
                    .query(r -> r.queryConditional(
                            sortBeginsWith(s
                                    -> s.partitionValue(clientId).sortValue(SortKeyType.WORKSHOP.name()).build()))
                    )
                    .items()
                    .stream()
                    .toList();

            return workshopDtos.stream()
                    .map(workshopDto ->
                            Workshop.builder()
                                    .clientId(workshopDto.getPartitionKey())
                                    .workshopId(StringUtils.remove(workshopDto.getSortKey(), SortKeyType.WORKSHOP.name()))
                                    .locationId(StringUtils.remove(workshopDto.getLocationId(), SortKeyType.LOCATION.name()))
                                    .organizerId(StringUtils.remove(workshopDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                                    .name(workshopDto.getName())
                                    .category(workshopDto.getCategory())
                                    .description(workshopDto.getDescription())
                                    .workshopDate(workshopDto.getWorkshopDate())
                                    .cost(workshopDto.getCost())
                                    .startTime(workshopDto.getStartTime())
                                    .duration(workshopDto.getDuration())
                                    .createdOn(workshopDto.getCreatedOn())
                                    .lastUpdatedOn(workshopDto.getLastUpdatedOn())
                                    .build()
                    ).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public void delete(String clientId, String workshopId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.WORKSHOP, workshopId);

            ddbTable.deleteItem(key);

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public Workshop create(Workshop workshop) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocalDateTime now = LocalDateTime.now();
            WorkshopDto workshopDto = WorkshopDto.builder()
                    .partitionKey(workshop.getClientId())
                    .sortKey(SortKeyType.WORKSHOP.name() + UniqueIdGenerator.generate())
                    .locationId(SortKeyType.LOCATION.name() + workshop.getLocationId())
                    .organizerId(SortKeyType.ORGANIZER.name() + workshop.getOrganizerId())
                    .name(workshop.getName())
                    .category(workshop.getCategory())
                    .description(workshop.getDescription())
                    .workshopDate(workshop.getWorkshopDate())
                    .cost(workshop.getCost())
                    .startTime(workshop.getStartTime())
                    .duration(workshop.getDuration())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            ddbTable.updateItem(workshopDto);

            return Workshop.builder()
                    .clientId(workshopDto.getPartitionKey())
                    .workshopId(StringUtils.remove(workshopDto.getSortKey(), SortKeyType.WORKSHOP.name()))
                    .locationId(StringUtils.remove(workshopDto.getLocationId(), SortKeyType.LOCATION.name()))
                    .organizerId(StringUtils.remove(workshopDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                    .name(workshopDto.getName())
                    .category(workshopDto.getCategory())
                    .description(workshopDto.getDescription())
                    .workshopDate(workshopDto.getWorkshopDate())
                    .cost(workshopDto.getCost())
                    .startTime(workshopDto.getStartTime())
                    .duration(workshopDto.getDuration())
                    .createdOn(workshopDto.getCreatedOn())
                    .lastUpdatedOn(workshopDto.getLastUpdatedOn())
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
     * @param workshop the object to replace the current database object
     */
    public Workshop update(Workshop workshop) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            WorkshopDto workshopDto = WorkshopDto.builder()
                    .partitionKey(workshop.getClientId())
                    .sortKey(SortKeyType.WORKSHOP.name() + workshop.getWorkshopId())
                    .locationId(SortKeyType.LOCATION.name() + workshop.getLocationId())
                    .organizerId(SortKeyType.ORGANIZER.name() + workshop.getOrganizerId())
                    .name(workshop.getName())
                    .category(workshop.getCategory())
                    .description(workshop.getDescription())
                    .workshopDate(workshop.getWorkshopDate())
                    .cost(workshop.getCost())
                    .startTime(workshop.getStartTime())
                    .duration(workshop.getDuration())
                    .createdOn(workshop.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            ddbTable.putItem(workshopDto);

            return Workshop.builder()
                    .clientId(workshopDto.getPartitionKey())
                    .workshopId(StringUtils.remove(workshopDto.getSortKey(), SortKeyType.WORKSHOP.name()))
                    .locationId(StringUtils.remove(workshopDto.getLocationId(), SortKeyType.LOCATION.name()))
                    .organizerId(StringUtils.remove(workshopDto.getOrganizerId(), SortKeyType.ORGANIZER.name()))
                    .name(workshopDto.getName())
                    .category(workshopDto.getCategory())
                    .description(workshopDto.getDescription())
                    .workshopDate(workshopDto.getWorkshopDate())
                    .cost(workshopDto.getCost())
                    .startTime(workshopDto.getStartTime())
                    .duration(workshopDto.getDuration())
                    .createdOn(workshopDto.getCreatedOn())
                    .lastUpdatedOn(workshopDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }
}
