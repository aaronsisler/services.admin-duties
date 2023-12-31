package com.ebsolutions.config;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotationStrategy extends HeaderColumnNameTranslateMappingStrategy {
    public AnnotationStrategy(Class<?> clazz) {
        Map<String, String> map = new HashMap<>();
        //To prevent the column sorting
        List<String> originalFieldOrder = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
            if (annotation != null) {
                map.put(annotation.column(), annotation.column());
                originalFieldOrder.add(annotation.column());
            }
        }
        setType(clazz);
        setColumnMapping(map);
        //Order the columns as they were created
//        setColumnOrderOnWrite((a, b) -> Integer.compare(originalFieldOrder.indexOf(a), originalFieldOrder.indexOf(b)));
        setColumnOrderOnWrite(Comparator.comparingInt(originalFieldOrder::indexOf));
    }

    @Override
    public String[] generateHeader(Object bean) throws CsvRequiredFieldEmptyException {
        String[] result = super.generateHeader(bean);
        for (int i = 0; i < result.length; i++) {
            result[i] = getColumnName(i);
        }
        return result;
    }
}

