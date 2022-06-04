package ru.itis.kpfu.rectangleproblem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.rectangleproblem.model.Scrap;
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
    public Page<Scrap> find(Double width, Double height) {
        return scrapRepository.findWithMaxHeightThatFits(width, height, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "height")));
    }
}
