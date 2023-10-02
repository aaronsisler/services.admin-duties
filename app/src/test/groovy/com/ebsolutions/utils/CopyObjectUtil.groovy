package com.ebsolutions.utils

import com.ebsolutions.models.*

class CopyObjectUtil {
    static Client client(Client client) {
        return Client.builder()
                .clientId(client.getClientId())
                .name(client.getName())
                .createdOn(client.getCreatedOn())
                .lastUpdatedOn(client.getLastUpdatedOn())
                .build()
    }

//    static Client json_client(Client client) {
//        ObjectMapper mapper = new ObjectMapper()
//        Client newClient = mapper.readValue(mapper.writeValueAsString(client), Client.class)
//        return newClient
//    }

    static Location location(Location location) {
        return Location.builder()
                .clientId(location.getClientId())
                .locationId(location.getLocationId())
                .name(location.getName())
                .createdOn(location.getCreatedOn())
                .lastUpdatedOn(location.getLastUpdatedOn())
                .build()
    }

    static Organizer organizer(Organizer organizer) {
        return Organizer.builder()
                .clientId(organizer.getClientId())
                .organizerId(organizer.getOrganizerId())
                .name(organizer.getName())
                .createdOn(organizer.getCreatedOn())
                .lastUpdatedOn(organizer.getLastUpdatedOn())
                .build()
    }

    static Event event(Event event) {
        return Event.builder()
                .clientId(event.getClientId())
                .eventId(event.getEventId())
                .dayOfWeek(event.getDayOfWeek())
                .locationId(event.getLocationId())
                .organizerId(event.getOrganizerId())
                .name(event.getName())
                .category(event.getCategory())
                .description(event.getDescription())
                .startTime(event.getStartTime())
                .duration(event.getDuration())
                .createdOn(event.getCreatedOn())
                .lastUpdatedOn(event.getLastUpdatedOn())
                .build()
    }

    static Workshop workshop(Workshop workshop) {
        return Workshop.builder()
                .clientId(workshop.getClientId())
                .workshopId(workshop.getWorkshopId())
                .workshopDate(workshop.getWorkshopDate())
                .cost(workshop.getCost())
                .locationId(workshop.getLocationId())
                .organizerId(workshop.getOrganizerId())
                .name(workshop.getName())
                .category(workshop.getCategory())
                .description(workshop.getDescription())
                .startTime(workshop.getStartTime())
                .duration(workshop.getDuration())
                .createdOn(workshop.getCreatedOn())
                .lastUpdatedOn(workshop.getLastUpdatedOn())
                .build()
    }
}
