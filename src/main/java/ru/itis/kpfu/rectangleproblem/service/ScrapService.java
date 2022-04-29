package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.repository.ScrapRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final GeometryFactory geometryFactory;
    private final GeometryService geometryService;

    @Transactional
    public Scrap cropScrap(Point bottomLeft, Point upperRight, Orientation orientation, boolean isEndFace, boolean isRectangle) {
        Scrap scrap = new Scrap();
        Polygon polygon = geometryService.createRectangularPolygon(bottomLeft, upperRight, orientation);

        scrap.setFigure(polygon);
        scrap.setHeight(geometryService.getShortestSide(polygon));
        scrap.setWidth(geometryService.getLongestSide(polygon));
        scrap.setOrientation(geometryService.computeOrientation(polygon));
        scrap.setEndFace(isEndFace);
        scrap.setRectangle(isRectangle);

        return scrapRepository.save(scrap);
    }

    @Transactional
    public void cropRectangleScrap(Scrap scrap, Rectangle rectangle, Coordinate newRectangleCoordinate) {
        Point scrapBottomLeft;
        Point scrapUpperRight;

        if (scrap.getOrientation() == Orientation.HORIZONTAL) {
            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(
                    newRectangleCoordinate.getX(),
                    newRectangleCoordinate.getY() + rectangle.getHeight()));
            scrapUpperRight = geometryFactory.createPoint(new Coordinate(
                    newRectangleCoordinate.getX() + rectangle.getWidth(),
                    scrap.getFigure().getCoordinates()[1].getY()));
        } else {
            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(
                    newRectangleCoordinate.getX() - rectangle.getHeight(),
                    newRectangleCoordinate.getY()));
            scrapUpperRight = geometryFactory.createPoint(new Coordinate(
                    scrap.getFigure().getCoordinates()[1].getX(),
                    newRectangleCoordinate.getY() + rectangle.getWidth()));
        }

        cropScrap(scrapBottomLeft, scrapUpperRight, scrap.getOrientation(), false, true);
    }

    @Transactional
    public void cropEndFaceScrap(Scrap scrap, Rectangle rightest) {
        Point scrapBottomLeft;
        Point scrapUpperRight;
        Orientation orientation;
        Coordinate[] scrapCoordinates = scrap.getFigure().getCoordinates();
        Coordinate[] rectangleCoordinates = rightest.getFigure().getCoordinates();

        if (scrap.getOrientation() == Orientation.HORIZONTAL) {
            orientation = Orientation.VERTICAL;
            scrapBottomLeft = geometryFactory.createPoint(scrapCoordinates[3]);
            scrapUpperRight = geometryFactory.createPoint(
                    new Coordinate(
                            rectangleCoordinates[2].getX(),
                            scrapCoordinates[1].getY())
            );
        } else {
            orientation = Orientation.HORIZONTAL;
            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(
                    scrapCoordinates[1].getX(),
                    rectangleCoordinates[2].getY()
            ));
            scrapUpperRight = geometryFactory.createPoint(scrapCoordinates[3]);
        }

        cropScrap(scrapBottomLeft, scrapUpperRight, orientation, true, false);
    }

    public Scrap findLargest() {
        return scrapRepository.findFirstByProcessedFalseOrderByHeightDesc();
    }

    public Scrap findLargest(Double height, Double width) {
        var roundedHeight = geometryService.round(height) - GeometryService.epsilon.doubleValue();
        var roundedWidth = geometryService.round(width) - GeometryService.epsilon.doubleValue();
        return scrapRepository.findFirstByProcessedFalseAndHeightGreaterThanEqualAndWidthGreaterThanEqualOrderByHeightAsc(roundedHeight, roundedWidth);
    }

    @Transactional
    public void save(Scrap scrap) {
        scrapRepository.save(scrap);
    }
}
