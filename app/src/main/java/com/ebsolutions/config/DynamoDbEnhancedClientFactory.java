package com.ebsolutions.config;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

public interface DynamoDbEnhancedClientFactory {
    DynamoDbEnhancedClient create();
}
