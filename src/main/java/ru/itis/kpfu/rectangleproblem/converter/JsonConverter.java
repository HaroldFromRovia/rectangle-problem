package ru.itis.kpfu.rectangleproblem.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.itis.kpfu.rectangleproblem.model.Polygon;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Zagir Dingizbaev
 */
@Converter
public class JsonConverter implements AttributeConverter<Polygon, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Polygon attribute) {
        return objectMapper.writeValueAsString(attribute);
    }

    @SneakyThrows
    @Override
    public Polygon convertToEntityAttribute(String dbData) {
        return objectMapper.readValue(dbData, Polygon.class);
    }
}
