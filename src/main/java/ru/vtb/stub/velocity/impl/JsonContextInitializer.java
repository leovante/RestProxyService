package ru.vtb.stub.velocity.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vtb.stub.velocity.ContextInitializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsonContextInitializer implements ContextInitializer {

    private final ObjectMapper mapper;

    /**
     * Initialize {@link VelocityContext} using params from input request
     *
     * @param request Request
     * @return Initialized {@link VelocityContext} using params from input request
     */
    @Override
    public VelocityContext init(String request) throws JsonProcessingException {
        VelocityContext context = new VelocityContext();
        JsonNode root = mapper.readTree(request);
        addKeys("", root, context, new ArrayList<>());
        addRandomLongs(context, 5);
        return context;
    }

    private void addKeys(String currentPath, JsonNode jsonNode, VelocityContext context, List<Integer> suffix) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + "_";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), context, suffix);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;

            for (int i = 0; i < arrayNode.size(); i++) {
                suffix.add(i + 1);
                addKeys(currentPath, arrayNode.get(i), context, suffix);

            }

        } else if (jsonNode.isValueNode()) {
            if (currentPath.contains("_")) {
                for (int i = 0; i < suffix.size(); i++) {
                    currentPath += "_" + suffix.get(i);
                }

                suffix = new ArrayList<>();
            }

            ValueNode valueNode = (ValueNode) jsonNode;
            context.put(currentPath, valueNode.asText());
        }
    }

    private void addRandomLongs(VelocityContext context, int size) {
        for (int i = 0; i < size; i++) {
            context.put("randomLong_" + i, (long) (Math.random() * Math.pow(10, 10)));
        }
    }


}
