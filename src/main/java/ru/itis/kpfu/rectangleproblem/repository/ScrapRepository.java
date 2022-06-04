package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Scrap findFirstByProcessedFalseOrderByHeightDesc();

    @Query("select s from Scrap s " +
            "where s.processed = false and s.width >= :width and s.height >= :height")
    Page<Scrap> findWithMaxHeightThatFits(Double width, Double height, Pageable pageable);

    @Query("select s from Scrap s " +
            "where s.processed = false and s.width >= :width and s.height >= :height")
    Page<Scrap> findWithMinHeightThatFits(Double width, Double height, Pageable pageable);

//    @Query("select s from Scrap s " +
//            "where s.processed = false " +
//            "and s.width >= :width " +
//            "and s.height >= :height " +
//            "order by s.height")
//    Optional<Scrap> findByProcessedFalseAndWidthGreaterThanAndHeightGreaterThanOrderByHeightAsc(@Param("width") Double width, @Param("height") Double height, Pageable pageable);
}
