package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.config.ShutdownManager;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * @author Zagir Dingizbaev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GeometryService {

    private final GeometryFactory factory;
    private final BigDecimal epsilon = new BigDecimal("0.0000000000000001");

    public Point createPoint(double x, double y) {
        return factory.createPoint(new Coordinate(x, y));
    }

    public Double getLongestSide(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();

        var dist1 = coordinates[0].distance(coordinates[1]);
        var dist2 = coordinates[1].distance(coordinates[2]);

        return round(Math.max(dist1, dist2));
    }

    public Double getShortestSide(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();

        var dist1 = coordinates[0].distance(coordinates[1]);
        var dist2 = coordinates[1].distance(coordinates[2]);

        return round(Math.min(dist1, dist2));
    }

    public Polygon createRectangularPolygon(Point bottomLeft, Point upperRight, Orientation orientation) {
        Coordinate[] coordinates = new Coordinate[5];

        if (orientation == Orientation.HORIZONTAL) {
            coordinates[0] = bottomLeft.getCoordinate();
            coordinates[1] = new Coordinate(bottomLeft.getX(), upperRight.getY());
            coordinates[2] = upperRight.getCoordinate();
            coordinates[3] = new Coordinate(upperRight.getX(), bottomLeft.getY());
            coordinates[4] = bottomLeft.getCoordinate();
        } else {
            coordinates[4] = bottomLeft.getCoordinate();
            coordinates[3] = new Coordinate(bottomLeft.getX(), upperRight.getY());
            coordinates[2] = upperRight.getCoordinate();
            coordinates[1] = new Coordinate(upperRight.getX(), bottomLeft.getY());
            coordinates[0] = bottomLeft.getCoordinate();
        }

        return factory.createPolygon(coordinates);
    }

    public Polygon createRectangularPolygon(Point bottomLeft, Point upperRight) {
        Coordinate[] coordinates = new Coordinate[5];

        coordinates[0] = bottomLeft.getCoordinate();
        coordinates[1] = new Coordinate(bottomLeft.getX(), upperRight.getY());
        coordinates[2] = upperRight.getCoordinate();
        coordinates[3] = new Coordinate(upperRight.getX(), bottomLeft.getY());
        coordinates[4] = bottomLeft.getCoordinate();

        return factory.createPolygon(coordinates);
    }

    public Point createPoint(Coordinate coordinate){
        return factory.createPoint(coordinate);
    }

    public Orientation computeOrientation(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();

        //Высота
        var dist1 = coordinates[0].distance(new Coordinate(coordinates[0].getX(), coordinates[2].getY()));
        //Ширина
        var dist2 = coordinates[0].distance(new Coordinate(coordinates[2].getX(), coordinates[0].getY()));

        return dist1 > dist2 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    public boolean covers(Polygon polygon, Point point) {
        var pointX = round(point.getX());
        var pointY = round(point.getY());
        var minX = round(Arrays.stream(polygon.getCoordinates())
                .min(Comparator.comparing(x -> round(x.getX())))
                .orElseThrow(NoSuchElementException::new)
                .getX());
        var minY = round(Arrays.stream(polygon.getCoordinates())
                .min(Comparator.comparing(x -> round(x.getY())))
                .orElseThrow(NoSuchElementException::new)
                .getY());

        var maxX = round(Arrays.stream(polygon.getCoordinates())
                .max(Comparator.comparing(x -> round(x.getX())))
                .orElseThrow(NoSuchElementException::new)
                .getX());
        var maxY = round(Arrays.stream(polygon.getCoordinates())
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
        bd = bd.setScale(15, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
