package com.ebsolutions.dal.utils;

import com.ebsolutions.dal.SortKeyType;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class KeyBuilder {
    public static Key build(String partitionKey, SortKeyType sortKeyType) {
        return Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKeyType.name())
                .build();
    }

    public static Key build(String partitionKey, SortKeyType sortKeyType, String sortKey) {
        return Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKeyType.name() + sortKey)
                .build();
    }
}
