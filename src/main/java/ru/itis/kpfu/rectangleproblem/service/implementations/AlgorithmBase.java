package ru.itis.kpfu.rectangleproblem.service.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.service.interfaces.LRPService;
import ru.itis.kpfu.rectangleproblem.service.interfaces.ScrapService;

@Service
@RequiredArgsConstructor
public class AlgorithmBase {

    private final LRPService lrpService;
    private final ScrapService scrapService;
    private static Long rectangleNumber = 0L;

    @Value("${step:upper-bound}")
    private Long stepUpperBound;
//
//    public void evaluate(){
//        lrpService.initLRP();
//
//        while(step < stepUpperBound){
//
//
//            step++;
//        }
//    }
}
