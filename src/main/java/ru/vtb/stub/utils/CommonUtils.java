package ru.vtb.stub.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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

    public static Object getData(Map<String, Map<String, Object>> map, String key, String service) {
        var data = map.get(key);
        if (data != null)
            log.debug("{} --> {}", service, data);
        else
            log.debug("{} --> {}", service, key);
        return data;
    }

    public static boolean removeOneKey(Map<String, Map<String, Object>> map, String key, String service) {
        var removedData = map.remove(key);
        if (removedData != null) {
            log.debug("{} --> {} deleted", service, removedData);
            return true;
        }
        else {
            log.debug("{}. No data for: {}", service, key);
            return false;
        }
    }

    public static boolean removeAllKeys(Map<String, Map<String, Object>> data, String keyStarts, Set<String> teams, String service) {
        String team = keyStarts.substring(1);
        if (!teams.contains(team)) {
            log.debug("{}. No team: '{}' in config file", service, team);
            return false;
        }
        var keysToDelete = data.keySet().stream().filter(k -> k.startsWith(keyStarts)).collect(Collectors.toSet());
        if (keysToDelete.isEmpty()) {
            log.debug("{}. No data for: {}", service, team);
            return false;
        }
        keysToDelete.forEach(k -> {
            data.remove(k);
            log.debug("{} --> {} deleted", service, k);
        });
        return true;
    }
}
