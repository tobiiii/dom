package com.itexc.dom.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    /**
     * @param list
     * @return
     */
    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return String.join(",", list);
    }

    /**
     * @param joined
     * @return
     */
    @Override
    public List<String> convertToEntityAttribute(String joined) {
        return new ArrayList<>(Arrays.asList(joined.split(",")));
    }

}