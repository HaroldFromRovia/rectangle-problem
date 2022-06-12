package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.exceptions.BoxNotFoundException;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

/**
 * @author Zagir Dingizbaev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PaulhusAlgorithm {

    private final ScrapService scrapService;
    private final GeometryService geometryService;
    private final LRPService lrpService;
    private final RectangleService rectangleService;
    private final AlgorithmProperties properties;

    public void evaluate(){
        scrapService.cropBox(geometryService.createPoint(0, 0.5), geometryService.createPoint(2.0 / 3, 1));
        rectangleService.placeInitial();
        while (rectangleService.getStep().get() < properties.getPaulhusUpperBound()) {
            try {
                Scrap box = scrapService.findThatFits(rectangleService.getWidth(), rectangleService.getHeight())
                        .orElseThrow(() -> new BoxNotFoundException(rectangleService.getStep().get()));
                Rectangle rectangle = scrapService.placeRectangle(box);
                if (rectangleService.getStep().get() % 1000 ==0){
                    log.info("Processed {}", rectangleService.getStep().get());
                }
                scrapService.saveNewBoxes(box, rectangle);
            } catch (Exception e) {
                break;
            }
        }

        Scrap paulhusLRP = scrapService.removeLRPFromScraps();
        lrpService.saveLRP(paulhusLRP);
    }
}
