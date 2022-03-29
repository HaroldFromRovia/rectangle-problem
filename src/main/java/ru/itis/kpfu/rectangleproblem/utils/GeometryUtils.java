package ru.itis.kpfu.rectangleproblem.utils;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.*;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

/**
 * @author Zagir Dingizbaev
 */

@UtilityClass
public class GeometryUtils {

    private static final GeometryFactory factory = new GeometryFactory();

    public static Double getLongestSide(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();

        var dist1 = coordinates[0].distance(coordinates[1]);
        var dist2 = coordinates[1].distance(coordinates[2]);

        return Math.max(dist1, dist2);
    }

    public static Double getShortestSide(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();

        var dist1 = coordinates[0].distance(coordinates[1]);
        var dist2 = coordinates[1].distance(coordinates[2]);

        return Math.min(dist1, dist2);
    }

    public static Polygon createRectangularPolygon(Point bottomLeft, Point upperRight) {
        Coordinate[] coordinates = new Coordinate[5];

        coordinates[0] = bottomLeft.getCoordinate();
        coordinates[1] = new Coordinate(bottomLeft.getX(), upperRight.getY());
        coordinates[2] = upperRight.getCoordinate();
        coordinates[3] = new Coordinate(upperRight.getX(), bottomLeft.getY());
        coordinates[4] = bottomLeft.getCoordinate();

        return factory.createPolygon(coordinates);
    }

    public static Orientation computeOrientation(Polygon polygon) {
        Coordinate[] coordinates = polygon.getCoordinates();

        var dist1 = coordinates[0].distance(coordinates[1]);
        var dist2 = coordinates[1].distance(coordinates[2]);

        return dist1 > dist2 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    public static Double getX(Rectangle rectangle) {
        var orientation = computeOrientation(rectangle.getFigure());
        if (orientation == Orientation.VERTICAL) {
            return rectangle.getFigure().getCoordinates()[2].getX();
        } else {
            return rectangle.getFigure().getCoordinates()[2].getY();
        }
    }
}
