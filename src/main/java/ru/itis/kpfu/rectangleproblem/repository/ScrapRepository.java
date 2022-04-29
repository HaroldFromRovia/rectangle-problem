package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Scrap findFirstByProcessedFalseOrderByHeightDesc();
    Scrap findFirstByProcessedFalseAndHeightGreaterThanEqualAndWidthGreaterThanEqualOrderByHeightAsc(Double height, Double width);
}
