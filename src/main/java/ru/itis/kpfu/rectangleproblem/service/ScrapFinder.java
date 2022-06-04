package ru.itis.kpfu.rectangleproblem.service;

import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.Optional;

/**
 * @author Zagir Dingizbaev
 */

public interface ScrapFinder {

    Optional<Scrap> find(Double width, Double height);
}
