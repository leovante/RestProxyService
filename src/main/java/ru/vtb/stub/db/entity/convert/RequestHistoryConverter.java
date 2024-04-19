package ru.vtb.stub.db.entity.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import ru.vtb.stub.domain.Request;

/**
 * RawMessageConverter.
 */
@Component
@RequiredArgsConstructor
public class RequestHistoryConverter implements AttributeConverter<Request, String> {

    private final ObjectMapper objectMapper;

    /**
     * convertToDatabaseColumn.
     *
     * @param attribute
     *         the entity attribute value to be converted
     *
     * @return String
     */
    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Request attribute) {
        if (attribute == null) {
            return null;
        }
        return objectMapper.writeValueAsString(attribute);
    }

    /**
     * convertToEntityAttribute.
     *
     * @param dbData
     *         the data from the database column to be converted
     *
     * @return JsonNode
     */
    @SneakyThrows
    @Override
    public Request convertToEntityAttribute(String dbData) {
        if (ObjectUtils.isEmpty(dbData)) {
            return null;
        }
        return objectMapper.readValue(dbData, Request.class);
    }

}
