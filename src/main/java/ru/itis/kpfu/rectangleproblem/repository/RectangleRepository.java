package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;

@Repository
public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
}
