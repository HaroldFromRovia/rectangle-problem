package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
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
            var scrapCandidate = scrapService.findLargestWidthMoreThan(rectangleService.getExtendedWidth(),
                    rectangleService.getExtendedHeight());
            if (scrapCandidate.isEmpty()) {
                lrpService.cropLRP();
                scrap = scrapService.findLargest();
                continue;
            }
            scrap = scrapCandidate.get();
        }
    }

    public void logProperties() {
        log.info("Current algorithm settings {}", algorithmProperties);
    }
}
