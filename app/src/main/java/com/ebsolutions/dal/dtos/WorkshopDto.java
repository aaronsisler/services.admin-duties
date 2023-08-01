package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Slf4j
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
@Serdeable
public class WorkshopDto extends DatabaseDto {
    @Getter(onMethod_ = @DynamoDbSortKey)
    @NonNull
    private String workshopId;
}
