package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.exceptions.BoxNotFoundException;
import ru.itis.kpfu.rectangleproblem.model.Box;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zagir Dingizbaev
 */

@Service
@RequiredArgsConstructor
public class PaulhusAlgorithmBase {

    private final LRPService lrpService;
    private final AlgorithmProperties algorithmProperties;
    private final RectangleService rectangleService;
    private final BoxService boxService;
    private final GeometryService geometryService;

    @PostConstruct
    public void evaluate() {
        lrpService.initLRP();
        placeInitial();
        while (rectangleService.getStep().get() < algorithmProperties.getUpperBound()) {
            Box box = boxService.findThatFits(rectangleService.getWidth(), rectangleService.getHeight())
                    .orElseThrow(() -> new BoxNotFoundException(rectangleService.getStep().get()));

            var rectangle = boxService.placeRectangle(box);
            boxService.saveNewBoxes(box, rectangle);
        }


    }

    private void placeInitial() {
        List<Rectangle> rectangleList = new ArrayList<>();
        boxService.cropBox(geometryService.createPoint(0, 0.5), geometryService.createPoint(2.0 / 3, 1));

        var firstRectangle = rectangleService.createRectangle();
        rectangleList.add(firstRectangle);
        firstRectangle.setFigure(geometryService.createRectangularPolygon(
                geometryService.createPoint(0, 0),
                geometryService.createPoint(1, 0.5)
        ));

        var secondRectangle = rectangleService.createRectangle();
        rectangleList.add(secondRectangle);
        secondRectangle.setFigure(geometryService.createRectangularPolygon(
                geometryService.createPoint(2.0 / 3, 0.5),
                geometryService.createPoint(1, 1)
        ));

        rectangleService.saveAll(rectangleList);
    }
}
