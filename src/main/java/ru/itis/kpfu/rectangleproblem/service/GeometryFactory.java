package ru.itis.kpfu.rectangleproblem.service;

import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.model.Coordinate;
import ru.itis.kpfu.rectangleproblem.model.Point;
import ru.itis.kpfu.rectangleproblem.model.Polygon;

import java.util.List;

/**
 * @author Zagir Dingizbaev
 */

@Service
public class GeometryFactory {
    public Point createPoint(Coordinate coordinate) {
        return new Point(coordinate.getX(), coordinate.getY());
    }

    public Polygon createPolygon(List<Coordinate> coordinates) {
        return new Polygon(coordinates);
    }
}
