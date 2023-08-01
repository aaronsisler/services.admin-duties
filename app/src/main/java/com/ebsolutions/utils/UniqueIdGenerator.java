package com.ebsolutions.utils;

import java.util.UUID;

public class UniqueIdGenerator {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
