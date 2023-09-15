package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.LocalDate;

@Data
@DynamoDbBean
@Serdeable
@Slf4j
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WorkshopDto extends BaseEventDto {
    @NonNull
    private LocalDate workshopDate;
    /**
     * Value in pennies
     */
    private short cost;
}
