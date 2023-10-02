package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseConstants;
import com.ebsolutions.dal.SortKeyType;
import com.ebsolutions.dal.dtos.ClientDto;
import com.ebsolutions.dal.utils.KeyBuilder;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Client;
import com.ebsolutions.models.MetricsStopWatch;
import com.ebsolutions.utils.UniqueIdGenerator;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Prototype
public class ClientDao {

    private final DynamoDbTable<ClientDto> ddbTable;

    public ClientDao(DynamoDbEnhancedClient enhancedClient) {
        this.ddbTable = enhancedClient.table(DatabaseConstants.DATABASE_TABLE_NAME, TableSchema.fromBean(ClientDto.class));
    }

    public Client read(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.CLIENT);

            ClientDto clientDto = ddbTable.getItem(key);

            return clientDto == null
                    ? null
                    : Client.builder()
                    .clientId(clientDto.getPartitionKey())
                    .name(clientDto.getName())
                    .createdOn(clientDto.getCreatedOn())
                    .lastUpdatedOn(clientDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "read"));
        }
    }

    public void delete(String clientId) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            Key key = KeyBuilder.build(clientId, SortKeyType.CLIENT);

            ddbTable.deleteItem(key);
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "delete"));
        }
    }

    public Client create(Client client) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            LocalDateTime now = LocalDateTime.now();

            ClientDto clientDto = ClientDto.builder()
                    .partitionKey(UniqueIdGenerator.generate())
                    .sortKey(SortKeyType.CLIENT.name())
                    .name(client.getName())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();

            ddbTable.updateItem(clientDto);

            return Client.builder()
                    .clientId(clientDto.getPartitionKey())
                    .name(clientDto.getName())
                    .createdOn(clientDto.getCreatedOn())
                    .lastUpdatedOn(clientDto.getLastUpdatedOn())
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
     * @param client the object to replace the current database object
     */
    public Client update(Client client) {
        MetricsStopWatch metricsStopWatch = new MetricsStopWatch();
        try {
            ClientDto clientDto = ClientDto.builder()
                    .partitionKey(client.getClientId())
                    .sortKey(SortKeyType.CLIENT.name())
                    .name(client.getName())
                    .createdOn(client.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();

            ddbTable.putItem(clientDto);

            return Client.builder()
                    .clientId(clientDto.getPartitionKey())
                    .name(clientDto.getName())
                    .createdOn(clientDto.getCreatedOn())
                    .lastUpdatedOn(clientDto.getLastUpdatedOn())
                    .build();
        } catch (Exception e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new DataProcessingException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        } finally {
            metricsStopWatch.logElapsedTime(MessageFormat.format("{0}::{1}", this.getClass().getName(), "update"));
        }
    }
}
