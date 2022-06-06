package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.config.ShutdownManager;
import ru.itis.kpfu.rectangleproblem.model.Box;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.repository.BoxRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Zagir Dingizbaev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class BoxService {

    private final GeometryService geometryService;
    private final BoxRepository boxRepository;
    private final RectangleService rectangleService;
    private final ShutdownManager shutdownManager;


    @Transactional
    public Box cropBox(Point bottomLeft, Point upperRight) {
        Box box = new Box();
        Polygon polygon = geometryService.createRectangularPolygon(bottomLeft, upperRight);

        box.setHeight(geometryService.getShortestSide(polygon));
        box.setWidth(geometryService.getLongestSide(polygon));
        box.setOrientation(geometryService.computeOrientation(polygon));
        box.setProcessed(false);
        box.setFigure(geometryService.createRectangularPolygon(bottomLeft, upperRight, box.getOrientation()));
        if (box.getHeight() == 0 || box.getWidth() == 0){
            return null;
        }

        return boxRepository.save(box);
    }

    @Transactional
    public Rectangle placeRectangle(Box box) {
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
            shutdownManager.initiateShutdown(-1);
        }

        Polygon figure = geometryService.createRectangularPolygon(bottomLeft, upperRight);
        rectangle.setFigure(figure);
        rectangleService.save(rectangle);
        boxRepository.setProcessed(box.getId());
        return  rectangle;
    }

    @Transactional
    public void saveNewBoxes(Box originalBox, Rectangle rectangle) {
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

    public Optional<Box> findThatFits(Double width, Double height) {
        return boxRepository.findThatFits(width, height);
    }

    public void save(Box box) {
        boxRepository.save(box);
    }
}
