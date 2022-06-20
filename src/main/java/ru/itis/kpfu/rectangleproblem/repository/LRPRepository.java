package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.LRP;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LRPRepository extends JpaRepository<LRP, Long> {

    @Modifying
    @Query("update LRP lrp SET lrp.step = :step, lrp.height = :height, lrp.width = :width where lrp.id =:id")
    LRP updateLrp(double height, double width, Long step, Long id);

    LRP findFirstByOrderByStepDesc();
}
