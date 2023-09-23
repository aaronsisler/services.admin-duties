package com.ebsolutions.utils

import com.ebsolutions.models.Client
import com.fasterxml.jackson.databind.ObjectMapper

class CopyObjectUtil {
    static Client client(Client client) {
        return Client.builder()
                .clientId(client.getClientId())
                .name(client.getName())
                .createdOn(client.getCreatedOn())
                .lastUpdatedOn(client.getLastUpdatedOn())
                .expiryDate(client.getExpiryDate())
                .build()
    }

    static Client json_client(Client client) {
        ObjectMapper mapper = new ObjectMapper()
        Client newClient = mapper.readValue(mapper.writeValueAsString(client), Client.class)
        return newClient
    }
}
