package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.config.ShutdownManager;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmBase {

    private final LRPService lrpService;
    private final GeometryService geometryService;
    private final ScrapService scrapService;
    private final RectangleService rectangleService;
    private final AlgorithmProperties algorithmProperties;
    private final GeometryFactory geometryFactory;
    private final ShutdownManager shutdownManager;

    @PostConstruct
    public void evaluate() {
        logProperties();
        lrpService.initLRP();
        lrpService.cropLRP(rectangleService.getStep().get());
        Scrap scrap = scrapService.findLargest();
        while (rectangleService.getStep().get() < algorithmProperties.getUpperBound()) {
            if (rectangleService.getStep().get() % 1000 == 0)
                log.info("Processed {}", rectangleService.getStep().get());

            var rightest = rectangleService.getRightestRectangle(scrap);
            var scrapCoordinates = scrap.getFigure().getCoordinates();
            Rectangle rectangle = rectangleService.createRectangle(rectangleService.getStep().get());

            Coordinate newRectangleCoordinate;
            if (rightest.isEmpty()) {
                newRectangleCoordinate = scrapCoordinates[0];
            } else {
                newRectangleCoordinate = rightest.get().getFigure().getCoordinates()[3];
            }

            Point rectangleBottomLeft = geometryFactory.createPoint(newRectangleCoordinate);
            Point rectangleUpperRight;

            Point extendedRectangleUpperRight;
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

            Polygon figure = geometryService.createRectangularPolygon(rectangleBottomLeft, rectangleUpperRight, scrap.getOrientation());
            Polygon scrapFigure = scrap.getFigure();
            if (!geometryService.covers(scrapFigure, extendedRectangleUpperRight)) {
                if (rightest.isEmpty()) {
                    log.error("Got null rightest on rectangle№ {}, scrap № {}", rectangleService.getStep().get(), scrap.getId());
                    log.info("Rectangle {} {}", rectangle.getHeight(), rectangle.getWidth());
                    log.info("Scrap {} {}", scrap.getHeight(), scrap.getWidth());
                    shutdownManager.initiateShutdown(-1);
                }
                scrapService.cropEndFaceScrap(scrap, rightest.get());

                scrap.setProcessed(true);
                scrapService.save(scrap);
                if (!scrap.isEndFace() && !scrap.isRectangle())
                    lrpService.cropLRP(rectangle.getIndex());
                var extendedHeight = rectangle.getHeight() + Math.pow(rectangle.getHeight(), algorithmProperties.getPower());
                var extendedWidth = rectangle.getWidth() + Math.pow(rectangle.getWidth(), algorithmProperties.getPower());
                scrap = scrapService.findLargest(extendedHeight, extendedWidth);
                continue;
            }

            rectangle.setFigure(figure);
            rectangle.setScrap(scrap);
            scrap.getRectangles().add(rectangle);

            rectangleService.save(rectangle);
            scrapService.cropRectangleScrap(scrap, rectangle, newRectangleCoordinate);

            rectangleService.getStep().incrementAndGet();
        }
    }

    public void logProperties(){
        log.info("Current algorithm settings {}", algorithmProperties);
    }
}
