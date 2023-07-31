package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Slf4j
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Serdeable
public abstract class DatabaseDto {
    @NonNull
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String clientId;
    @NonNull
    private String createdOn;
    @NonNull
    private String lastUpdatedOn;
}
