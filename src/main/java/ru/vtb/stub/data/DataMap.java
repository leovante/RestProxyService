package ru.vtb.stub.data;

import ru.vtb.stub.domain.Request;
import ru.vtb.stub.domain.StubData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMap {

    public static Map<String, StubData> dataMap = new HashMap<>();
    public static Map<String, List<Request>> requestMap = new HashMap<>();
}
