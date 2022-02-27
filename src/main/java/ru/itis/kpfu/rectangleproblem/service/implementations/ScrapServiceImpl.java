package ru.itis.kpfu.rectangleproblem.service.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.repository.ScrapRepository;
import ru.itis.kpfu.rectangleproblem.service.interfaces.ScrapService;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final ScrapRepository scrapRepository;

    @Override
    @Transactional
    public Scrap cropScrap(Point bottomLeft, Point upperRight) {
        Scrap scrap = new Scrap();

        scrap.setBottomLeft(bottomLeft);
        scrap.setUpperRight(upperRight);

        return scrapRepository.save(scrap);
    }

    @Override
    public Scrap insertRectangle(Scrap scrap, Rectangle rectangle) {
        scrap.getRectangles().add(rectangle);

        return scrapRepository.save(scrap);
    }

    @Override
    public Scrap insertRectangles(Scrap scrap, List<Rectangle> rectangles) {
        scrap.getRectangles().addAll(rectangles);
        return scrapRepository.save(scrap);
    }
}
