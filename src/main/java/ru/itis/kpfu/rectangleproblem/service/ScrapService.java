package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.config.ShutdownManager;
import ru.itis.kpfu.rectangleproblem.model.*;
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
    private final ShutdownManager shutdownManager;
    private final AlgorithmProperties algorithmProperties;
    private final GeometryService geometryService;
    private final ScrapFinder scrapFinder;

    @Transactional
    public Scrap fillScrap(Scrap scrap) {
        var scrapCoordinates = scrap.getFigure().getCoordinates();
        if (!scrap.getRectangles().isEmpty()) {
            log.error("Got rectangles in scrap {}", scrap.getId());
            shutdownManager.initiateShutdown(-1);
        }
        Point extendedRectangleUpperRight;
        Polygon scrapFigure = scrap.getFigure();
        List<Rectangle> rectangles = new ArrayList<>();

        do {
            Rectangle rectangle = rectangleService.createRectangle();
            Coordinate newRectangleCoordinate;
            if (rectangles.isEmpty()) {
                newRectangleCoordinate = scrapCoordinates.get(0);
            } else {
                newRectangleCoordinate = rectangles.get(rectangles.size() - 1).getFigure().getCoordinates().get(3);
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
            rectangle.setScrap(scrap);

            rectangles.add(rectangle);
        } while ((geometryService.covers(scrapFigure, extendedRectangleUpperRight)));

        rectangleService.getStep().decrementAndGet();
        rectangles.remove(rectangles.size() - 1);
        rectangles.forEach(
                r -> {
                    cropRectangleScrap(scrap, r, r.getFigure().getCoordinates().get(0));
                    if (r.getIndex() % 1000 == 0) {
                        log.info("Processed {}", r.getIndex());
                    }
                }
        );
        if (rectangles.isEmpty()) {
            return scrap;
        }

        scrap.setProcessed(true);
        scrap.setRectangles(rectangles);

        cropEndFaceScrap(scrap, rectangles.get(rectangles.size() - 1));
        return scrapRepository.save(scrap);
    }

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
                    scrap.getFigure().getCoordinates().get(1).getY()));
        } else {
            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(
                    newRectangleCoordinate.getX() - rectangle.getHeight(),
                    newRectangleCoordinate.getY()));
            scrapUpperRight = geometryFactory.createPoint(new Coordinate(
                    scrap.getFigure().getCoordinates().get(1).getX(),
                    newRectangleCoordinate.getY() + rectangle.getWidth()));
        }

        cropScrap(scrapBottomLeft, scrapUpperRight, scrap.getOrientation(), false, true);
    }

    @Transactional
    public void cropEndFaceScrap(Scrap scrap, Rectangle rightest) {
        Point scrapBottomLeft;
        Point scrapUpperRight;
        Orientation orientation;
        var scrapCoordinates = scrap.getFigure().getCoordinates();
        var rectangleCoordinates = rightest.getFigure().getCoordinates();

        if (scrap.getOrientation() == Orientation.HORIZONTAL) {
            orientation = Orientation.VERTICAL;
            scrapBottomLeft = geometryFactory.createPoint(scrapCoordinates.get(3));
            scrapUpperRight = geometryFactory.createPoint(
                    new Coordinate(
                            rectangleCoordinates.get(2).getX(),
                            scrapCoordinates.get(1).getY())
            );
        } else {
            orientation = Orientation.HORIZONTAL;
            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(
                    scrapCoordinates.get(1).getX(),
                    rectangleCoordinates.get(2).getY()
            ));
            scrapUpperRight = geometryFactory.createPoint(scrapCoordinates.get(3));
        }

        cropScrap(scrapBottomLeft, scrapUpperRight, orientation, true, false);
    }

    @Transactional
    public void cropEndFaceScrap(Scrap scrap) {
        cropEndFaceScrap(scrap, scrap.getRectangles().get(scrap.getRectangles().size() - 1));
    }

    public Scrap findLargest() {
        return scrapRepository.findFirstByProcessedFalseOrderByHeightDesc();
    }

    public Optional<Scrap> findLargestWidthMoreThan(Double width, Double height) {
        return scrapFinder.find(width, height);
    }

    @Transactional
    public Scrap save(Scrap scrap) {
        return scrapRepository.save(scrap);
    }
}
