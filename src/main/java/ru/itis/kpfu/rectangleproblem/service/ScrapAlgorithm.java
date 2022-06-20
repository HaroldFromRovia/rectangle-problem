package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.exceptions.ScrapOutOfLRPBoundsException;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

/**
 * @author Zagir Dingizbaev
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapAlgorithm {

    private final ScrapService scrapService;
    private final LRPService lrpService;
    private final RectangleService rectangleService;
    private final AlgorithmProperties properties;

    @SneakyThrows
    public void evaluate() {

        while (rectangleService.getStep().get() < properties.getUpperBound()) {
            try {
                Scrap scrap = getLargest();
                scrapService.fillScrap(scrap);
            } catch (Exception e) {
                log.error("Exception", e);
                break;
            }
        }
    }

    private Scrap getLargest() throws ScrapOutOfLRPBoundsException {
        var scrapCandidate = scrapService.findLargestThatCanFit(rectangleService.getExtendedWidth(),
                rectangleService.getExtendedHeight());
        if (scrapCandidate.isEmpty()) {
            lrpService.cropLRP();
            return scrapService.findLargest();
        }
        return scrapCandidate.get();
    }
}
