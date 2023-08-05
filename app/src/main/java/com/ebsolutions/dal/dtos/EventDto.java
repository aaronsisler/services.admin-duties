package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.sql.Time;

@Data
@DynamoDbBean
@Serdeable
@Slf4j
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto extends DatabaseDto {
    @NonNull
    @Getter(onMethod_ = @DynamoDbSortKey)
    private String eventId;
    @NonNull
    private String name;
    private String category;
    @NonNull
    private String description;
    @NonNull
    private byte dayOfWeek;
    @NonNull
    private Time startTime;
    @NonNull
    private short duration;
}
