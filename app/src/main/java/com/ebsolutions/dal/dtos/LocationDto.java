package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
@Serdeable
@Slf4j
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto extends DatabaseDto {
    @NonNull
    @Getter(onMethod_ = @DynamoDbSortKey)
    private String locationId;
    @NonNull
    private String name;
}
