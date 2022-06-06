package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.config.ShutdownManager;
import ru.itis.kpfu.rectangleproblem.exceptions.CannotFitRectangleException;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.repository.ScrapRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final GeometryFactory geometryFactory;
    private final RectangleService rectangleService;
    private final AlgorithmProperties algorithmProperties;
    private final GeometryService geometryService;
    private final ScrapFinder scrapFinder;

    @Transactional
    public void fillScrap(Scrap scrap) {
        Coordinate[] scrapCoordinates = scrap.getFigure().getCoordinates();
        Point extendedRectangleUpperRight;
        Polygon scrapFigure = scrap.getFigure();
        List<Rectangle> rectangles = new ArrayList<>();

        do {
            Rectangle rectangle = rectangleService.createRectangle();
            Coordinate newRectangleCoordinate;
            if (rectangles.isEmpty()) {
                newRectangleCoordinate = scrapCoordinates[0];
            } else {
                newRectangleCoordinate = rectangles.get(rectangles.size() - 1).getFigure().getCoordinates()[3];
            }
            Point rectangleBottomLeft = geometryFactory.createPoint(newRectangleCoordinate);
            Point rectangleUpperRight;

            if (scrap.getOrientation() == Orientation.HORIZONTAL) {
                rectangleUpperRight = geometryFactory.createPoint(new Coordinate(
                        newRectangleCoordinate.getX() + rectangle.getWidth(),
                        newRectangleCoordinate.getY() + rectangle.getHeight()));
                extendedRectangleUpperRight = geometryFactory.createPoint(new Coordinate(
                        rectangleUpperRight.getX() + Math.pow(rectangle.getWidth(), algorithmProperties.getPower()),
                        rectangleUpperRight.getY() + Math.pow(rectangle.getHeight(), algorithmProperties.getPower())));
            } else {
                rectangleUpperRight = geometryFactory.createPoint(new Coordinate(
                        newRectangleCoordinate.getX() - rectangle.getHeight(),
                        newRectangleCoordinate.getY() + rectangle.getWidth()));
                extendedRectangleUpperRight = geometryFactory.createPoint(new Coordinate(
                        rectangleUpperRight.getX() - Math.pow(rectangle.getHeight(), algorithmProperties.getPower()),
                        rectangleUpperRight.getY() + Math.pow(rectangle.getWidth(), algorithmProperties.getPower())));
            }


            Polygon rectangleFigure = geometryService.createRectangularPolygon(rectangleBottomLeft, rectangleUpperRight, scrap.getOrientation());
            rectangle.setFigure(rectangleFigure);

            rectangles.add(rectangle);
        } while ((geometryService.covers(scrapFigure, extendedRectangleUpperRight)));

        rectangleService.getStep().decrementAndGet();
        rectangles.remove(rectangles.size() - 1);
        rectangles.forEach(
                r -> {
                    cropRectangleScrap(scrap, r, r.getFigure().getCoordinates()[0]);
                    if (r.getIndex() % 10000 == 0) {
                        log.info("Processed {}", r.getIndex());
                    }
                }
        );
        if (rectangles.isEmpty()) {
            return;
        }

        cropEndFaceScrap(scrap, rectangles.get(rectangles.size() - 1));
        scrapRepository.setProcessed(scrap.getId());
    }

    @Transactional
    public Scrap cropScrap(Point bottomLeft, Point upperRight, Orientation orientation, boolean isEndFace, boolean isRectangle) {
        return cropScrap(bottomLeft, upperRight, orientation, isEndFace, isRectangle, false);
    }

    @Transactional
    public Scrap cropScrap(Point bottomLeft, Point upperRight, Orientation orientation, boolean isEndFace, boolean isRectangle, boolean isProcessed) {
        Scrap scrap = new Scrap();
        Polygon polygon = geometryService.createRectangularPolygon(bottomLeft, upperRight, orientation);

        scrap.setFigure(polygon);
        scrap.setHeight(geometryService.getShortestSide(polygon));
        scrap.setWidth(geometryService.getLongestSide(polygon));
        scrap.setOrientation(geometryService.computeOrientation(polygon));
        scrap.setEndFace(isEndFace);
        scrap.setRectangle(isRectangle);
        scrap.setProcessed(isProcessed);

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

    //Box like scraps
    @Transactional
    public Scrap cropBox(Point bottomLeft, Point upperRight) {
        Scrap box = new Scrap();
        Polygon polygon = geometryService.createRectangularPolygon(bottomLeft, upperRight);

        box.setHeight(geometryService.getShortestSide(polygon));
        box.setWidth(geometryService.getLongestSide(polygon));
        box.setOrientation(geometryService.computeOrientation(polygon));
        box.setProcessed(false);
        box.setFigure(geometryService.createRectangularPolygon(bottomLeft, upperRight, box.getOrientation()));
        if (box.getHeight() == 0 || box.getWidth() == 0) {
            return null;
        }

        return scrapRepository.save(box);
    }

    @Transactional
    public Rectangle placeRectangle(Scrap box) throws CannotFitRectangleException {
        Coordinate lowerRight = box.getOrientation() == Orientation.HORIZONTAL ? box.getFigure().getCoordinates()[3] : box.getFigure().getCoordinates()[1];
        var rectangle = rectangleService.createRectangle();
        Point bottomLeft = null;
        Point upperRight = null;

        Point verticalCandidate = geometryService.createPoint(lowerRight.getX() - rectangle.getHeight(), lowerRight.getY() + rectangle.getWidth());
        if (geometryService.covers(box.getFigure(), verticalCandidate)) {
            bottomLeft = geometryService.createPoint(verticalCandidate.getX(), lowerRight.getY());
            upperRight = geometryService.createPoint(lowerRight.getX(), verticalCandidate.getY());
        }

        Point horizontalCandidate = geometryService.createPoint(lowerRight.getX() - rectangle.getWidth(), lowerRight.getY() + rectangle.getHeight());
        if (geometryService.covers(box.getFigure(), horizontalCandidate)) {
            bottomLeft = geometryService.createPoint(horizontalCandidate.getX(), lowerRight.getY());
            upperRight = geometryService.createPoint(lowerRight.getX(), horizontalCandidate.getY());
        }

        if (bottomLeft == null || upperRight == null) {
            log.error("Cannot fit value which is strange");
            throw new CannotFitRectangleException();
        }

        Polygon figure = geometryService.createRectangularPolygon(bottomLeft, upperRight);
        rectangle.setFigure(figure);
        rectangleService.save(rectangle);
        scrapRepository.setProcessed(box.getId());
        return rectangle;
    }

    @Transactional
    public void saveNewBoxes(Scrap originalBox, Rectangle rectangle) {
        Coordinate[] boxCoordinates = originalBox.getFigure().getCoordinates();
        Coordinate rectangleUpperLeft = rectangle.getFigure().getCoordinates()[1];
        Point firstBoxBottomLeft = geometryService.createPoint(boxCoordinates[0]);
        Point secondBoxUpperRight = geometryService.createPoint(boxCoordinates[2]);
        Point firstBoxUpperRight;
        Point secondBoxBottomLeft;

        if (originalBox.getOrientation() == Orientation.VERTICAL) {
            firstBoxUpperRight = geometryService.createPoint(rectangleUpperLeft);
            secondBoxBottomLeft = geometryService.createPoint(boxCoordinates[0].getX(), rectangleUpperLeft.getY());
        } else {
            firstBoxUpperRight = geometryService.createPoint(rectangleUpperLeft.getX(), boxCoordinates[1].getY());
            secondBoxBottomLeft = geometryService.createPoint(rectangleUpperLeft);
        }
        cropBox(firstBoxBottomLeft, firstBoxUpperRight);
        cropBox(secondBoxBottomLeft, secondBoxUpperRight);
    }

    public Optional<Scrap> findThatFits(Double width, Double height) {
        return scrapRepository.findThatFits(width, height);
    }

    @Transactional
    public void removeLRP() {
        var scrap = scrapRepository.findLargest();
        log.info("Scrap to remove {}", scrap.getId());
        scrapRepository.delete(scrap);
    }
}
