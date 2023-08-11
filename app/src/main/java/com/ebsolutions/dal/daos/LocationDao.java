package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseTables;
import com.ebsolutions.dal.dtos.LocationDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Location;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Prototype
public class LocationDao {

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<LocationDto> locationTable;

    public LocationDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.locationTable = this.enhancedClient.table(DatabaseTables.LOCATION, TableSchema.fromBean(LocationDto.class));
    }

    public Location read(String clientId, String locationId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(locationId).build();

            LocationDto locationDto = locationTable.getItem(key);

            return locationDto == null
                    ? null
                    : Location.builder()
                    .clientId(locationDto.getClientId())
                    .locationId(locationDto.getLocationId())
                    .name(locationDto.getName())
                    .createdOn(locationDto.getCreatedOn())
                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
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

    public List<Location> readAll(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).build();
            QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
            List<LocationDto> locationDtos = locationTable.query(queryConditional).items().stream().collect(Collectors.toList());

            return locationDtos.stream()
                    .map(locationDto ->
                            Location.builder()
                                    .clientId(locationDto.getClientId())
                                    .locationId(locationDto.getLocationId())
                                    .name(locationDto.getName())
                                    .createdOn(locationDto.getCreatedOn())
                                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
                                    .build()
                    ).collect(Collectors.toList());

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "readAll"));
        }
    }

    public void delete(String clientId, String locationId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = Key.builder().partitionValue(clientId).sortValue(locationId).build();

            locationTable.deleteItem(key);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "delete"));
        }
    }

    public Location create(Location location) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocalDateTime now = LocalDateTime.now();
            LocationDto locationDto = LocationDto.builder()
                    .clientId(location.getClientId())
                    .locationId(UniqueIdGenerator.generate())
                    .name(location.getName())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            locationTable.updateItem(locationDto);

            return Location.builder()
                    .clientId(locationDto.getClientId())
                    .locationId(locationDto.getLocationId())
                    .name(locationDto.getName())
                    .createdOn(locationDto.getCreatedOn())
                    .lastUpdatedOn(locationDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "create"));
        }
    }

    /**
     * This will replace the entire database object with the input client
     *
     * @param location the object to replace the current database object
     */
    public void update(Location location) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocationDto locationDto = LocationDto.builder()
                    .clientId(location.getClientId())
                    .locationId(location.getLocationId())
                    .name(location.getName())
                    .createdOn(location.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            locationTable.putItem(locationDto);

        } catch (DynamoDbException dbe) {
            log.error("ERROR::{}", this.getClass().getName(), dbe);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), dbe);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException("Error in {}".formatted(this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "update"));
        }
    }
}
