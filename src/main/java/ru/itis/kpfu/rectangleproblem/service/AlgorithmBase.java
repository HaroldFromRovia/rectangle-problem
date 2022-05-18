package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.config.ShutdownManager;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmBase {

    private final LRPService lrpService;
    private final ScrapService scrapService;
    private final RectangleService rectangleService;
    private final AlgorithmProperties algorithmProperties;


    @PostConstruct
    public void evaluate() {
        logProperties();
        lrpService.initLRP();
        lrpService.cropLRP(rectangleService.getStep().get());
        Scrap scrap = scrapService.findLargest();
        while (rectangleService.getStep().get() < algorithmProperties.getUpperBound()) {
            scrapService.fillScrap(scrap);
            scrap = scrapService.findLargest();
            if (scrap.getRectangles().isEmpty()) {
                lrpService.cropLRP();
            }
        }
    }

    public void logProperties() {
        log.info("Current algorithm settings {}", algorithmProperties);
    }
}
