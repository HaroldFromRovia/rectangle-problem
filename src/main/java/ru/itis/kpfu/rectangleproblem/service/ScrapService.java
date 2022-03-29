package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;
import ru.itis.kpfu.rectangleproblem.repository.ScrapRepository;
import ru.itis.kpfu.rectangleproblem.utils.GeometryUtils;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ru.itis.kpfu.rectangleproblem.utils.GeometryUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    @Transactional
    public Scrap cropScrap(Point bottomLeft, Point upperRight) {
        Scrap scrap = new Scrap();
        Polygon polygon = createRectangularPolygon(bottomLeft, upperRight);

        scrap.setFigure(polygon);
        scrap.setHeight(getShortestSide(polygon));
        scrap.setWidth(getLongestSide(polygon));

        return scrapRepository.save(scrap);
    }

    public Scrap findLargest() {
        return scrapRepository.findFirstByOrderByWidth();
    }

}
