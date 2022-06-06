package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.Box;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.Optional;

/**
 * @author Zagir Dingizbaev
 */

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {

    @Query(value = "SELECT * FROM {h-schema}box b " +
            "WHERE b.processed = FALSE " +
            "AND ((b.width >= :width AND b.height >= :height) OR (:width <= b.height AND :height <= b.width)) " +
            "ORDER BY b.height " +
            "LIMIT 1", nativeQuery = true)
    Optional<Box> findThatFits(Double width, Double height);

    @Modifying
    @Query(value = "UPDATE {h-schema}box SET processed=True WHERE id=:id", nativeQuery = true)
    void setProcessed(Long id);
}
