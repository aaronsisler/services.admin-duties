package com.ebsolutions.dal.dtos;

import io.micronaut.serde.annotation.Serdeable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
@Serdeable
public class ClientDto {

    @Getter(onMethod_ = @DynamoDbPartitionKey)
    @NonNull
    @Setter
    private String clientId;

    @Getter
    @NonNull
    @Setter
    private String name;

    @NonNull
    @Setter
    @Getter
    private LocalDateTime createdOn;

    @Getter
    @NonNull
    @Setter
    private String lastUpdatedOn;
}
