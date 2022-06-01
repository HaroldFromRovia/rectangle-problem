package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.model.Coordinate;
import ru.itis.kpfu.rectangleproblem.model.Point;
import ru.itis.kpfu.rectangleproblem.model.Polygon;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author Zagir Dingizbaev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GeometryService {

    private final GeometryFactory factory;
    private final AlgorithmProperties algorithmProperties;
    private final BigDecimal epsilon = new BigDecimal("0.0000000000000001");

    public Double extend(Double side) {
        return Math.pow(side, algorithmProperties.getPower());
    }

    public Double getLongestSide(Polygon polygon) {
        var coordinates = polygon.getCoordinates();

        var dist1 = coordinates.get(0).distance(coordinates.get(1));
        var dist2 = coordinates.get(1).distance(coordinates.get(2));

        return Math.max(dist1, dist2);
    }

    public Double getShortestSide(Polygon polygon) {
        var coordinates = polygon.getCoordinates();

        var dist1 = coordinates.get(0).distance(coordinates.get(1));
        var dist2 = coordinates.get(1).distance(coordinates.get(2));

        return Math.min(dist1, dist2);
    }

    public Polygon createRectangularPolygon(Point bottomLeft, Point upperRight, Orientation orientation) {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate());
        coordinates.add(new Coordinate());
        coordinates.add(new Coordinate());
        coordinates.add(new Coordinate());
        coordinates.add(new Coordinate());

        if (orientation == Orientation.HORIZONTAL) {
            coordinates.set(0, bottomLeft.getCoordinate());
            coordinates.set(1, new Coordinate(bottomLeft.getX(), upperRight.getY()));
            coordinates.set(2, upperRight.getCoordinate());
            coordinates.set(3, new Coordinate(upperRight.getX(), bottomLeft.getY()));
            coordinates.set(4, bottomLeft.getCoordinate());
        } else {
            coordinates.set(4, bottomLeft.getCoordinate());
            coordinates.set(3, new Coordinate(bottomLeft.getX(), upperRight.getY()));
            coordinates.set(2, upperRight.getCoordinate());
            coordinates.set(1, new Coordinate(upperRight.getX(), bottomLeft.getY()));
            coordinates.set(0, bottomLeft.getCoordinate());
        }

        return factory.createPolygon(coordinates);
    }

    public Orientation computeOrientation(Polygon polygon) {
        var coordinates = polygon.getCoordinates();

        //Высота
        var dist1 = coordinates.get(0).distance(new Coordinate(coordinates.get(0).getX(), coordinates.get(2).getY()));
        //Ширина
        var dist2 = coordinates.get(0).distance(new Coordinate(coordinates.get(2).getX(), coordinates.get(0).getY()));

        return dist1 > dist2 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    public boolean covers(Polygon polygon, Point point) {
        var pointX = round(point.getX());
        var pointY = round(point.getY());
        var minX = round(polygon.getCoordinates()
                .stream()
                .min(Comparator.comparing(x -> round(x.getX())))
                .orElseThrow(NoSuchElementException::new)
                .getX());
        var minY = round(polygon.getCoordinates()
                .stream()
                .min(Comparator.comparing(x -> round(x.getY())))
                .orElseThrow(NoSuchElementException::new)
                .getY());

        var maxX = round(polygon.getCoordinates()
                .stream()
                .max(Comparator.comparing(x -> round(x.getX())))
                .orElseThrow(NoSuchElementException::new)
                .getX());
        var maxY = round(polygon.getCoordinates()
                .stream()
                .max(Comparator.comparing(x -> round(x.getY())))
                .orElseThrow(NoSuchElementException::new)
                .getY());

        return pointX >= minX - epsilon.doubleValue() &&
                pointX <= maxX + epsilon.doubleValue() &&
                pointY >= minY - epsilon.doubleValue() &&
                pointY <= maxY + epsilon.doubleValue();
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(16, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
