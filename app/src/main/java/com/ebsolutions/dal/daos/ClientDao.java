package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseTables;
import com.ebsolutions.dal.dtos.ClientDto;
import com.ebsolutions.exceptions.DataProcessingException;
import com.ebsolutions.models.Client;
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
public class ClientDao {

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<ClientDto> clientTable;

    public ClientDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.clientTable = this.enhancedClient.table(DatabaseTables.CLIENT, TableSchema.fromBean(ClientDto.class));
    }

    public ClientDto read(String clientId) {
        try {
            Key key = Key.builder().partitionValue(clientId).build();
            ClientDto clientDto = clientTable.getItem(key);
            return clientDto;
        } catch (DynamoDbException dbe) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), dbe);
            throw new DataProcessingException("Error in ClientDao", dbe);
        } catch (Exception e) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), e);
            throw new DataProcessingException("Error in ClientDao", e);
        }
    }

    public void delete(String clientId) {
        try {
            Key key = Key.builder().partitionValue(clientId).build();
            clientTable.deleteItem(key);
        } catch (DynamoDbException dbe) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), dbe);
            throw new DataProcessingException("Error in ClientDao", dbe);
        } catch (Exception e) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), e);
            throw new DataProcessingException("Error in ClientDao", e);
        }
    }

    public Client create(Client client) {
        try {
            LocalDateTime now = LocalDateTime.now();
            ClientDto clientDto = ClientDto.builder()
                    .name(client.getName())
                    .clientId(UniqueIdGenerator.generate())
                    .createdOn(now)
                    .lastUpdatedOn(now)
                    .build();
            clientTable.updateItem(clientDto);

            return Client.builder()
                    .clientId(clientDto.getClientId())
                    .name(clientDto.getName())
                    .createdOn(clientDto.getCreatedOn())
                    .lastUpdatedOn(clientDto.getLastUpdatedOn())
                    .build();
        } catch (DynamoDbException dbe) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), dbe);
            throw new DataProcessingException("Error in ClientDao", dbe);
        } catch (Exception e) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), e);
            throw new DataProcessingException("Error in ClientDao", e);
        }
    }

    /**
     * This will replace the entire database object with the input client
     *
     * @param client the object to replace the current database object
     */
    public void update(Client client) {
        try {
            ClientDto clientDto = ClientDto.builder()
                    .clientId(client.getClientId())
                    .name(client.getName())
                    .createdOn(client.getCreatedOn())
                    .lastUpdatedOn(LocalDateTime.now())
                    .build();
            clientTable.putItem(clientDto);
        } catch (DynamoDbException dbe) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), dbe);
            throw new DataProcessingException("Error in ClientDao", dbe);
        } catch (Exception e) {
            log.error("ERROR::ClientDao::{}", this.getClass().getEnclosingMethod().getName(), e);
            throw new DataProcessingException("Error in ClientDao", e);
        }
    }
}
