package com.ebsolutions.utils

import com.ebsolutions.models.Client
import com.ebsolutions.models.Location
import com.ebsolutions.models.Organizer
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

    static Location location(Location location) {
        return Location.builder()
                .clientId(location.getClientId())
                .locationId(location.getLocationId())
                .name(location.getName())
                .createdOn(location.getCreatedOn())
                .lastUpdatedOn(location.getLastUpdatedOn())
                .expiryDate(location.getExpiryDate())
                .build()
    }

    static Organizer organizer(Organizer organizer) {
        return Organizer.builder()
                .clientId(organizer.getClientId())
                .organizerId(organizer.getOrganizerId())
                .name(organizer.getName())
                .createdOn(organizer.getCreatedOn())
                .lastUpdatedOn(organizer.getLastUpdatedOn())
                .expiryDate(organizer.getExpiryDate())
                .build()
    }
}
