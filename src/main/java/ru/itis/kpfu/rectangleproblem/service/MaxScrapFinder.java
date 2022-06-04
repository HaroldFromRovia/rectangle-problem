package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
import ru.itis.kpfu.rectangleproblem.model.ScrapView;
import ru.itis.kpfu.rectangleproblem.repository.ScrapRepository;

import java.util.Optional;

/**
 * @author Zagir Dingizbaev
 */

@Profile("!min")
@Service
@RequiredArgsConstructor
public class MaxScrapFinder implements ScrapFinder {

    private final ScrapRepository scrapRepository;

    @Override
    public Optional<Scrap> find(Double width, Double height) {
        return scrapRepository.findWithMaxHeightThatFits(width, height);
    }
}
