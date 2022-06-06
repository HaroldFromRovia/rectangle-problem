package ru.itis.kpfu.rectangleproblem.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.repository.RectangleRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author Zagir Dingizbaev
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RectangleService {

    @Getter
    private final AtomicLong step = new AtomicLong(1L);
    private final RectangleRepository rectangleRepository;
    private final AlgorithmProperties algorithmProperties;


    public Double getWidth(){
        return  1 / (step.get() - 1 + algorithmProperties.getSize() * algorithmProperties.getSize() + 1);
    }

    public Double getHeight(){
        return  1 / (step.get() - 1 + algorithmProperties.getSize() * algorithmProperties.getSize());
    }

    public Double getExtendedWidth() {
        double width = 1 / (step.get() - 1 + algorithmProperties.getSize() * algorithmProperties.getSize() + 1);
        return width + Math.pow(width, algorithmProperties.getPower());
    }

    public Double getExtendedHeight() {
        double height = 1 / (step.get() - 1 + algorithmProperties.getSize() * algorithmProperties.getSize());
        return height + Math.pow(height, algorithmProperties.getPower());
    }

    public Rectangle createRectangle() {
        double height = 1 / (step.get() - 1 + algorithmProperties.getSize() * algorithmProperties.getSize());
        double width = 1 / (step.get() - 1 + algorithmProperties.getSize() * algorithmProperties.getSize() + 1);

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(width);
        rectangle.setIndex(step.getAndIncrement());
        rectangle.setHeight(height);

        return rectangle;
    }

    @Transactional
    public void saveAll(List<Rectangle> rectangles) {
        rectangleRepository.saveAll(rectangles);
    }
}
