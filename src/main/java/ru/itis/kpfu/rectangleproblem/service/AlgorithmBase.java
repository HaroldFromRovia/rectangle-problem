package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.exceptions.BoxNotFoundException;
import ru.itis.kpfu.rectangleproblem.exceptions.ScrapOutOfLRPBoundsException;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmBase {

    private final LRPService lrpService;
    private final ScrapService scrapService;
    private final RectangleService rectangleService;
    private final AlgorithmProperties properties;
    private final GeometryService geometryService;


    @SneakyThrows
    @PostConstruct
    public void evaluate() {
        logProperties();
        lrpService.initLRP();
        scrapService.cropBox(geometryService.createPoint(0, 0.5), geometryService.createPoint(2.0 / 3, 1));
        rectangleService.placeInitial();
        while (rectangleService.getStep().get() < properties.getPaulhusUpperBound()) {
            try {
                Scrap box = scrapService.findThatFits(rectangleService.getWidth(), rectangleService.getHeight())
                        .orElseThrow(() -> new BoxNotFoundException(rectangleService.getStep().get()));
                Rectangle rectangle = scrapService.placeRectangle(box);
                scrapService.saveNewBoxes(box, rectangle);
                if (rectangleService.getStep().get() % 1000 == 0) {
                    log.info("Processing {}", rectangleService.getStep().get());
                }
            } catch (Exception e) {
                break;
            }
        }

        scrapService.removeLRP();

//        lrpService.cropLRP(rectangleService.getStep().get());
//        Scrap scrap = scrapService.findLargest();
//        while (rectangleService.getStep().get() < properties.getUpperBound()) {
//            try {
//                scrapService.fillScrap(scrap);
//                var scrapCandidate = scrapService.findLargestWidthMoreThan(rectangleService.getExtendedWidth(),
//                        rectangleService.getExtendedHeight());
//                if (scrapCandidate.isEmpty()) {
//                    lrpService.cropLRP();
//                    scrap = scrapService.findLargest();
//                    continue;
//                }
//                scrap = scrapCandidate.get();
//            } catch (Exception e) {
//                break;
//            }
//        }
    }


    public void logProperties() {
        log.info("Current algorithm settings {}", properties);
    }
}
