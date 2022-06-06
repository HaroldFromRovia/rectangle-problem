package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.exceptions.ScrapNotFoundException;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
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
    private final ScrapService scrapService;
    private final GeometryService geometryService;

    @PostConstruct
    public void evaluate() {
        lrpService.initLRP();
        Long step = rectangleService.getStep().get();
        placeInitial();
        while (step < algorithmProperties.getUpperBound()) {
            Scrap scrap = scrapService.findThatFits(rectangleService.getWidth(), rectangleService.getHeight())
                    .orElseThrow(() -> new ScrapNotFoundException(step));
        }
    }

    private void placeInitial() {
        List<Rectangle> rectangleList = new ArrayList<>();
        scrapService.cropScrap(geometryService.createPoint(0, 0.5), geometryService.createPoint(2.0 / 3, 1),
                Orientation.HORIZONTAL, false, false, true);

        var firstRectangle = rectangleService.createRectangle();
        rectangleList.add(firstRectangle);
        firstRectangle.setFigure(geometryService.createRectangularPolygon(geometryService.createPoint(0,0),
                geometryService.createPoint(1,0.5),Orientation.HORIZONTAL));

        var secondRectangle = rectangleService.createRectangle();
        rectangleList.add(secondRectangle);
        secondRectangle.setFigure(geometryService.createRectangularPolygon(geometryService.createPoint(2.0/3, 0.5),
                geometryService.createPoint(1,1),Orientation.VERTICAL));

        rectangleService.saveAll(rectangleList);
    }
}
