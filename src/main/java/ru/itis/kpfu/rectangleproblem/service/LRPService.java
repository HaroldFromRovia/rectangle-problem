package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.model.LRP;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.repository.LRPRepository;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class LRPService {

    private final LRPRepository lrpRepository;
    private final ScrapService scrapService;
    private final GeometryFactory geometryFactory;
    private final AtomicLong step = new AtomicLong();
    private final AlgorithmProperties properties;

    private Double size;


    @Transactional
    public void initLRP() {
        LRP lrp = new LRP();
        this.size = 1 / properties.getSize();

        lrp.setStep(step.get());
        lrp.setHeight(this.size);
        lrp.setWidth(this.size);

        lrpRepository.save(lrp);
    }

    public LRP getCurrent() {
        return lrpRepository.findFirstByOrderByStepDesc();
    }

    @Transactional
    public void cropLRP(Long index) {
        LRP current = lrpRepository.findFirstByOrderByStepDesc();
        LRP newLRP = new LRP();

        double rectangleHeight = 1 / (properties.getSize() * properties.getSize() + index - 1);
        double extendedRectangleHeight = rectangleHeight + Math.pow(rectangleHeight, properties.getPower());
        Point scrapBottomLeft;
        Point scrapUpperRight;
        Orientation orientation;

        //Режем справа
        if (current.getHeight() < current.getWidth()) {
            double newLRPWidth = current.getWidth() - extendedRectangleHeight;
            orientation = Orientation.VERTICAL;
            newLRP.setWidth(newLRPWidth);
            newLRP.setHeight(current.getHeight());

            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(current.getWidth(), this.size - current.getHeight()));
            scrapUpperRight = geometryFactory.createPoint(new Coordinate(newLRPWidth, this.size));
            //Режем снизу
        } else {
            double newLRPHeight = current.getHeight() - extendedRectangleHeight;
            newLRP.setHeight(newLRPHeight);
            newLRP.setWidth(current.getWidth());
            orientation = Orientation.HORIZONTAL;

            scrapBottomLeft = geometryFactory.createPoint(new Coordinate(0, this.size - current.getHeight()));
            scrapUpperRight = geometryFactory.createPoint(new Coordinate(current.getWidth(), this.size - newLRPHeight));
        }

        newLRP.setStep(step.incrementAndGet());
        newLRP.setRectangleIndex(index);

        scrapService.cropScrap(scrapBottomLeft, scrapUpperRight, orientation, false, false);
        lrpRepository.save(newLRP);
    }

}
