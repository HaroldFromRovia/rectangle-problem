package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Scrap findFirstByProcessedFalseOrderByHeightDesc();

    @Query(value = "SELECT * FROM {h-schema}scrap s " +
            "WHERE s.processed = FALSE " +
            "AND s.height >= :height " +
            "ORDER BY s.height DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Scrap> findWithMaxHeightThatFits(Double height);

    @Query(value = "SELECT * FROM {h-schema}scrap s " +
            "WHERE s.processed = FALSE " +
            "AND s.width >= :width " +
            "AND s.height >= :height " +
            "ORDER BY s.height " +
            "LIMIT 1", nativeQuery = true)
    Optional<Scrap> findWithMinHeightThatFits(Double width, Double height);

    @Query(value = "SELECT * FROM {h-schema}scrap s " +
            "WHERE s.processed = FALSE " +
            "AND ((s.width >= :width AND s.height >= :height) OR (:width <= s.height AND :height <= s.width)) " +
            "ORDER BY s.height " +
            "LIMIT 1", nativeQuery = true)
    Optional<Scrap> findThatFits(Double width, Double height);

    @Query(value = "SELECT * FROM {h-schema}scrap s " +
            "WHERE s.processed = FALSE " +
            "ORDER BY s.height DESC " +
            "LIMIT 1", nativeQuery = true)
    Scrap findLargest();

    @Modifying
    @Query(value = "UPDATE {h-schema}scrap SET processed=True WHERE id=:id", nativeQuery = true)
    void setProcessed(Long id);
}
