package ru.itis.kpfu.rectangleproblem.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.model.LRP;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.repository.LRPRepository;
import ru.itis.kpfu.rectangleproblem.service.interfaces.LRPService;
import ru.itis.kpfu.rectangleproblem.service.interfaces.ScrapService;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LRPServiceImpl implements LRPService {

    private final LRPRepository lrpRepository;
    private final ScrapService scrapService;
    private final GeometryFactory geometryFactory;

    private static Long step = 0L;
    private static final Double power = 1.5;

    @Value("${initial.size}")
    private Double n;
    private Double size;

    @Transactional
    public LRP initLRP() {
        LRP lrp = new LRP();
        this.size = 1 / n;

        lrp.setStep(0L);
        lrp.setHeight(this.size);
        lrp.setWidth(this.size);

        return lrpRepository.save(lrp);
    }

    @Override
    public LRP getCurrent() {
        return lrpRepository.findFirstByOrderByStepDesc();
    }

    @Override
    @Transactional
    public LRP cropLRP(Rectangle rectangle) {
        LRP current = lrpRepository.findFirstByOrderByStepDesc();
        LRP newLRP = new LRP();

        double rectangleHeight = rectangle.getUpperRight().getY() - rectangle.getBottomLeft().getY();
        double extendedRectangleHeight = Math.pow(rectangleHeight, power);
        Point bottomLeft;
        Point upperRight;

        //Режем справа
        if (current.getHeight() < current.getWidth()) {
            double newWidth = current.getWidth() - extendedRectangleHeight;
            newLRP.setWidth(newWidth);

            bottomLeft = geometryFactory.createPoint(new Coordinate(newWidth, this.size - current.getHeight()));
            upperRight = geometryFactory.createPoint(new Coordinate(current.getWidth(), this.size));
            //Режем снизу
        } else {
            Double newHeight = current.getHeight() - extendedRectangleHeight;
            newLRP.setHeight(newHeight);

            bottomLeft = geometryFactory.createPoint(new Coordinate(0, this.size - current.getHeight()));
            upperRight = geometryFactory.createPoint(new Coordinate(current.getWidth(), this.size - newHeight));
        }

        step++;
        newLRP.setStep(step);

        scrapService.cropScrap(bottomLeft, upperRight);
        return lrpRepository.save(newLRP);
    }
}
