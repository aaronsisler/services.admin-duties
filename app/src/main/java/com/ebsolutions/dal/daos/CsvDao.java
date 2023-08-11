package com.ebsolutions.dal.daos;

import com.ebsolutions.config.Constants;
import com.ebsolutions.dal.dtos.CalendarEventDto;
import com.ebsolutions.exceptions.CsvGenerationException;
import com.ebsolutions.models.CalendarEvent;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.micronaut.context.annotation.Prototype;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.List;


@Slf4j
@Prototype
public class CsvDao {
    public void create(List<CalendarEvent> calendarEvents, String filePath) {

        List<CalendarEventDto> calendarEventDtos = this.convertCalendarEventsToDtos(calendarEvents);

        log.info("calendarEventDtos: {}", calendarEventDtos);
        try (Writer writer = new FileWriter(filePath)) {

            StatefulBeanToCsv<CalendarEventDto> sbc = new StatefulBeanToCsvBuilder<CalendarEventDto>(writer)
                    .withQuotechar('\'')
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();


            sbc.write(calendarEventDtos);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            log.error("ERROR::{}", this.getClass().getName(), e);
            throw new CsvGenerationException(MessageFormat.format("Error in {0}", this.getClass().getName()), e);
        }
    }

    private List<CalendarEventDto> convertCalendarEventsToDtos(List<CalendarEvent> calendarEvents) {
        return calendarEvents.stream()
                .map(calendarEvent ->
                        CalendarEventDto.builder()
                                .eventOrganizerName(
                                        calendarEvent.getOrganizer() != null
                                                ? calendarEvent.getOrganizer().getName()
                                                : Constants.STRING_EMPTY)
                                .eventStartDate(calendarEvent.getEventDate().toString())
                                .EventStartTime(calendarEvent.getEvent().getStartTime().toString())
                                .eventLength(String.valueOf(calendarEvent.getEvent().getDuration()))
                                .eventEndDate(calendarEvent.getEventDate().toString())
                                .eventEndTime(calendarEvent
                                        .getEvent()
                                        .getStartTime()
                                        .plusMinutes(calendarEvent
                                                .getEvent()
                                                .getDuration()).toString())
                                .eventName(calendarEvent.getEvent().getName())
                                .eventCategory(calendarEvent.getEvent().getCategory())
                                .eventVenueName(calendarEvent.getLocation().getName())
                                .eventDescription(calendarEvent.getEvent().getDescription())
                                .build()
                ).toList();
    }
}
