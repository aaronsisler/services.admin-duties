package com.ebsolutions.dal.dtos;

import com.opencsv.bean.CsvBindByName;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@Serdeable
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDto {
    @CsvBindByName(column = "Event Organizer Name")
    private String eventOrganizerName;
    @CsvBindByName(column = "Event Start Date")
    private String eventStartDate;
    @CsvBindByName(column = "Event Start Time")
    private String EventStartTime;
    @CsvBindByName(column = "Event Length")
    private String eventLength;
    @CsvBindByName(column = "Event End Date")
    private String eventEndDate;
    @CsvBindByName(column = "Event End Time")
    private String eventEndTime;
    @CsvBindByName(column = "Event Name")
    private String eventName;
    @CsvBindByName(column = "Event Category")
    private String eventCategory;
    @CsvBindByName(column = "Event Venue Name")
    private String eventVenueName;
    @CsvBindByName(column = "Event Description")
    private String eventDescription;
}
