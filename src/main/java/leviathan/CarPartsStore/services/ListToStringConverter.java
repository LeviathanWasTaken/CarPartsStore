package leviathan.CarPartsStore.services;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return String.join(DELIMITER, list);
    }

    @Override
    public List<String> convertToEntityAttribute(String joinedString) {
        if (joinedString == null || joinedString.isEmpty()) {
            return null;
        }
        return Arrays.stream(joinedString.split(DELIMITER))
                .collect(Collectors.toList());
    }
}
