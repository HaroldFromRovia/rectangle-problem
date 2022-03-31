package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.repository.ScrapRepository;

import static ru.itis.kpfu.rectangleproblem.utils.GeometryUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    @Transactional
    public Scrap cropScrap(Point bottomLeft, Point upperRight, Orientation orientation) {
        Scrap scrap = new Scrap();
        Polygon polygon = createRectangularPolygon(bottomLeft, upperRight, orientation);

        scrap.setFigure(polygon);
        scrap.setHeight(getShortestSide(polygon));
        scrap.setWidth(getLongestSide(polygon));
        scrap.setOrientation(orientation);

        return scrapRepository.save(scrap);
    }

    public Scrap findLargest() {
        return scrapRepository.findFirstByProcessedFalseOrderByWidthDesc();
    }

    @Transactional
    public void save(Scrap scrap){
        scrapRepository.save(scrap);
    }
}
