package ru.vtb.stub.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtils {

    public static final String ALL = "ALL";
    public static final String KEY_DELIMITER = ":";

    public static Map<String, String> getRequestQueryParams(String queryString) {
        return Arrays.stream(queryString.split("&"))
                .map(p -> p.split("="))
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }

    public static boolean removeOneKey(Map<String, Map<String, Object>> map, String key, String service) {
        var removedData = map.remove(key);
        if (removedData != null) {
            log.debug("{}. Deleted data: {}", service, removedData);
            return true;
        }
        else {
            log.debug("{}. No data for: {}", service, key);
            return false;
        }
    }

    public static boolean removeAllKeys(Map<String, Map<String, Object>> data, String keyStarts, String team, String service) {
        var keysToDelete = data.keySet().stream().filter(k -> k.startsWith(keyStarts)).collect(Collectors.toSet());
        if (keysToDelete.isEmpty()) {
            log.debug("{}. No data for: {}", service, team);
            return false;
        }
        keysToDelete.forEach(k -> {
            data.remove(k);
            log.debug("{}. Deleted data: {}", service, k);
        });
        return true;
    }
}
