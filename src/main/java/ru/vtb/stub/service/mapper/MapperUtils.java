package ru.vtb.stub.service.mapper;

import io.micronaut.context.annotation.Bean;
import jakarta.inject.Singleton;
import ru.vtb.stub.domain.StubData;

@Singleton
public class MapperUtils {

    private static final String TEMPLATE = "--";

    public static String buildTeam(StubData team) {
        return team.getTeam() != null ? team.getTeam().toLowerCase() : null;
    }

    public static String buildRegexKey(StubData pk) {
        if (isRegex(pk.getPath())) {
            return replaceKey(pk.getPath());
        }
        return pk.getPath();
    }

    public static String buildRegexKey(String pk) {
        if (isRegex(pk)) {
            return replaceKey(pk);
        }
        return pk;
    }

    private static String replaceKey(String key) {
        return "^"
                + key.replaceAll(TEMPLATE, "[a-zA-Z0-9.@%/_-]+").replaceAll("/", "\\/")
                + "$";
    }

    public static boolean isRegex(String path) {
        return path.contains(TEMPLATE);
    }

}
