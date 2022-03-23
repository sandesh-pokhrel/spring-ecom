package com.kavka.apiservices.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Component
public class GeneralUtil {

    public String getSerialNumber(int count) {
        return new SecureRandom().ints(0, 36)
                .mapToObj(i -> Integer.toString(i, 36))
                .map(String::toUpperCase).distinct().limit(16).collect(Collectors.joining())
                .replaceAll("([A-Z0-9]{4})", "$1-").substring(0, count*4+count-1);
    }
}
