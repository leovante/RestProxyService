package ru.vtb.stub.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonUtils {

    public static Map<String, String> getRequestQueryParams(String queryString) {
        return Arrays.stream(queryString.split("&"))
                .map(p -> p.split("="))
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }
}
