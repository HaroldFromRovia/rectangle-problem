package ru.itis.kpfu.rectangleproblem.model;

import org.locationtech.jts.geom.Polygon;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

/**
 * @author Zagir Dingizbaev
 */
public interface ScrapView {

    Polygon getFigure();
    Orientation getOrientation();
    Long getId();

}
