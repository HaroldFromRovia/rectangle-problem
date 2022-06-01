package ru.itis.kpfu.rectangleproblem.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.itis.kpfu.rectangleproblem.model.Coordinate;
import ru.itis.kpfu.rectangleproblem.model.Polygon;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zagir Dingizbaev
 */

@Converter
public class PolygonConverter implements AttributeConverter<Polygon, String> {

    private final String DELIMITER = ",";
    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(Polygon attribute) {
        return attribute.getCoordinates()
                .stream()
                .map(Coordinate::toString)
                .collect(Collectors.joining(DELIMITER));
    }

    @SneakyThrows
    @Override
    public Polygon convertToEntityAttribute(String dbData) {
        var split = dbData.split(DELIMITER);
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < split.length; i += 2) {
            coordinates.add(new Coordinate(Double.parseDouble(split[i]), Double.parseDouble(split[i+1])));
        }
        return new Polygon(coordinates);
    }
}
