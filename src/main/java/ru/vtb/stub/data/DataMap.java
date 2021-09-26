package ru.vtb.stub.data;

import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;

import java.util.*;

public class DataMap {

    public static Map<String, StubData> dataByKeyMap = new HashMap<>();
    public static Map<String, StubData> dataByRegexMap = new TreeMap<>((o1, o2) -> {
        var p1 = o1.replaceAll("\\[a-zA-Z0-9_-]\\+", "zzz");
        var p2 = o2.replaceAll("\\[a-zA-Z0-9_-]\\+", "zzz");
        return p2.compareTo(p1);
    });
    public static Map<String, List<Request>> requestMap = new HashMap<>();
}
