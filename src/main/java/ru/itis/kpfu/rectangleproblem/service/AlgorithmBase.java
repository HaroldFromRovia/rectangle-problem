package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.utils.GeometryUtils;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmBase {

    private final LRPService lrpService;
    private final ScrapService scrapService;
    private final RectangleService rectangleService;
    private final AlgorithmProperties algorithmProperties;
    private final GeometryFactory geometryFactory;

    @Transactional
    @PostConstruct
    public void evaluate() {
        lrpService.initLRP();
        lrpService.cropLRP(rectangleService.getStep().get());
        while (rectangleService.getStep().get() < algorithmProperties.getUpperBound()) {
            Scrap scrap = scrapService.findLargest();

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
            if (GeometryUtils.computeOrientation(scrap.getFigure()) == Orientation.HORIZONTAL) {
                rectangleUpperRight = geometryFactory.createPoint(new Coordinate(
                        newRectangleCoordinate.getX() + rectangle.getWidth(),
                        newRectangleCoordinate.getY() + rectangle.getHeight()));
            } else {
                rectangleUpperRight = geometryFactory.createPoint(new Coordinate(
                        newRectangleCoordinate.getX() - rectangle.getHeight(),
                        newRectangleCoordinate.getY() + rectangle.getWidth()));
            }
            var figure = GeometryUtils.createRectangularPolygon(rectangleBottomLeft, rectangleUpperRight);
            if (scrap.getFigure().contains(figure)){
                System.out.println(rectangle.getIndex());
            }
            rectangle.setFigure(figure);
            rectangle.setScrap(scrap);
            rectangleService.save(rectangle);

            rectangleService.getStep().incrementAndGet();
        }
    }
}
