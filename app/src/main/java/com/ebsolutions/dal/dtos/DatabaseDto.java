package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Slf4j
@Data
@DynamoDbBean
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Serdeable
public abstract class DatabaseDto {
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    @NonNull
    private String clientId;

    @NonNull
    private LocalDateTime createdOn;

    @NonNull
    private LocalDateTime lastUpdatedOn;
}
