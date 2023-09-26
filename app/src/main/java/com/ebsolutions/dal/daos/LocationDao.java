package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseConstants;
import com.ebsolutions.dal.SortKeyType;
import com.ebsolutions.dal.dtos.LocationDto;
import com.ebsolutions.dal.utils.KeyBuilder;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Location;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional.sortBeginsWith;

@Slf4j
@Prototype
public class LocationDao {
    private final DynamoDbTable<LocationDto> ddbTable;

    public LocationDao(DynamoDbEnhancedClient enhancedClient) {
        this.ddbTable = enhancedClient.table(DatabaseConstants.DATABASE_TABLE_NAME, TableSchema.fromBean(LocationDto.class));
    }

    public Location read(String clientId, String locationId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.LOCATION, locationId);

            LocationDto locationDto = ddbTable.getItem(key);

            return locationDto == null
                    ? null
                    : Location.builder()
                    .clientId(locationDto.getPartitionKey())
                    .locationId(StringUtils.remove(locationDto.getSortKey(), SortKeyType.LOCATION.name()))
                    .name(locationDto.getName())
                    .createdOn(locationDto.getCreatedOn())
                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public List<Location> readAll(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            List<LocationDto> locationDtos = ddbTable
                    .query(r -> r.queryConditional(
                            sortBeginsWith(s
                                    -> s.partitionValue(clientId).sortValue(SortKeyType.LOCATION.name()).build()))
                    )
                    .items()
                    .stream()
                    .toList();

            return locationDtos.stream()
                    .map(locationDto ->
                            Location.builder()
                                    .clientId(locationDto.getPartitionKey())
                                    .locationId(StringUtils.remove(locationDto.getSortKey(), SortKeyType.LOCATION.name()))
                                    .name(locationDto.getName())
                                    .createdOn(locationDto.getCreatedOn())
                                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
                                    .build()
                    ).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "readAll"));
        }
    }

    public void delete(String clientId, String locationId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.LOCATION, locationId);

            ddbTable.deleteItem(key);

        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "delete"));
        }
    }

    public Location create(Location location) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocationDto locationDto = LocationDto.builder()
                    .partitionKey(location.getClientId())
                    .sortKey(SortKeyType.LOCATION.name() + UniqueIdGenerator.generate())
                    .name(location.getName())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            ddbTable.updateItem(locationDto);

            return Location.builder()
                    .clientId(locationDto.getPartitionKey())
                    .locationId(StringUtils.remove(locationDto.getSortKey(), SortKeyType.LOCATION.name()))
                    .name(locationDto.getName())
                    .createdOn(locationDto.getCreatedOn())
                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
                    .build();
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
     * @param location the object to replace the current database object
     */
    public Location update(Location location) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocationDto locationDto = LocationDto.builder()
                    .partitionKey(location.getClientId())
                    .sortKey(SortKeyType.LOCATION.name() + location.getLocationId())
                    .name(location.getName())
                    .createdOn(location.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            ddbTable.putItem(locationDto);

            return Location.builder()
                    .clientId(locationDto.getPartitionKey())
                    .locationId(StringUtils.remove(locationDto.getSortKey(), SortKeyType.LOCATION.name()))
                    .name(locationDto.getName())
                    .createdOn(locationDto.getCreatedOn())
                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "update"));
        }
    }
}
