package com.ebsolutions.dal.daos;

import com.ebsolutions.config.DatabaseTables;
import com.ebsolutions.dal.dtos.ClientDto;
import com.ebsolutions.exceptions.DataRetrievalException;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@Slf4j
@Prototype
public class ClientDao {

    private DynamoDbEnhancedClient enhancedClient;

    public ClientDao(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    public ClientDto get(String clientId) {
        try {
            DynamoDbTable<ClientDto> clientTable = this.enhancedClient.table(DatabaseTables.CLIENT, TableSchema.fromBean(ClientDto.class));
            Key key = Key.builder().partitionValue(clientId).build();
            ClientDto clientDto = clientTable.getItem(key);
            return clientDto;
        } catch (DynamoDbException dbe) {
            log.error("Error in ClientDao", dbe);
            throw new DataRetrievalException("Error in ClientDao", dbe);
        } catch (Exception e) {
            log.error("Error in ClientDao", e);
            throw new DataRetrievalException("Error in ClientDao", e);
        }
    }
}
