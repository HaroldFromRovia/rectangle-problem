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
public class Main {

    private final AlgorithmProperties properties;
    private final PaulhusAlgorithm paulhusAlgorithm;
    private final LRPService lrpService;
    private final ScrapAlgorithm scrapAlgorithm;

    @PostConstruct
    public void evaluate() {
        lrpService.initLRP();
        logProperties();
//        paulhusAlgorithm.evaluate();
        scrapAlgorithm.evaluate();
    }

    public void logProperties() {
        log.info("Current algorithm settings {}", properties);
    }
}
