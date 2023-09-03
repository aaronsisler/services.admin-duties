package com.ebsolutions.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.context.annotation.Requires;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Slf4j
@Factory
@Requires(env = "local-docker")
public class DynamoDbDockerLocalConfig {
    private final String awsAccessKeyId = "access_key_id";
    private final String awsSecretAccessKey = "secret_access_key";
    private final String endpoint = "http://dynamo-db-local:8000";

    @Prototype
    public DynamoDbEnhancedClient create() {
        log.info("Here creating the Local DDB Beans");
        URI localEndpoint = URI.create(endpoint);
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(localEndpoint)
                .credentialsProvider(staticCredentialsProvider)
                .build();

        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }
}
