package ru.itis.kpfu.rectangleproblem.service.interfaces;

import org.locationtech.jts.geom.Point;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.List;

public interface ScrapService {

    Scrap cropScrap(Point bottomLeft, Point upperRight);

    Scrap insertRectangle(Scrap scrap, Rectangle rectangle);

    Scrap insertRectangles(Scrap scrap,List<Rectangle> rectangle);
}
