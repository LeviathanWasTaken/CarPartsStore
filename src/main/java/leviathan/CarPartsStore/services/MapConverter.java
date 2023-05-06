package leviathan.CarPartsStore.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;
import java.util.HashMap;

public class MapConverter implements AttributeConverter<HashMap<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(HashMap<String, Object> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Error converting hashmap to JSON string ", e);
            }
        }
    }

    @Override
    public HashMap<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData.isEmpty()) {
            return new HashMap<>();
        } else {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(dbData, HashMap.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error converting JSON string to HashMap", e);
            }
        }
    }
}

