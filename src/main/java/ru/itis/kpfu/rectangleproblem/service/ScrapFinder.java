package ru.itis.kpfu.rectangleproblem.service;

import org.springframework.data.domain.Page;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.Optional;

/**
 * @author Zagir Dingizbaev
 */
public interface ScrapFinder {

    Page<Scrap> find(Double width, Double height);
}
