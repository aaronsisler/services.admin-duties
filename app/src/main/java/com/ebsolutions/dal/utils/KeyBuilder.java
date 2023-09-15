package com.ebsolutions.dal.utils;

import software.amazon.awssdk.enhanced.dynamodb.Key;

public class KeyBuilder {
    public static Key build(String partitionKey, String sortKeyPrefix, String sortKey) {
        return Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKeyPrefix + sortKey)
                .build();
    }
}
