package ru.vtb.stub.service.mapper;

import org.springframework.stereotype.Component;
import ru.vtb.stub.domain.StubData;
import ru.vtb.stub.dto.GetDataBaseRequest;

@Component
public class MapperUtils {

    private static final String TEMPLATE = "--";

    public static String buildTeam(StubData team) {
        return team.getTeam().toLowerCase();
    }

    public static String buildRegexKey(StubData pk) {
        if (isRegex(pk.getPath())) {
            return replaceKey(pk.getPath());
        }
        return pk.getPath();
    }

    private static String replaceKey(String key) {
        return key.replaceAll(TEMPLATE, "[a-zA-Z0-9.@%/_-]+")
                .replaceAll("/", "\\/") + "$";
    }

    public static boolean isRegex(String path) {
        return path.contains(TEMPLATE);
    }

}
