package ru.itis.kpfu.rectangleproblem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.kpfu.rectangleproblem.model.Scrap;

import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Scrap findFirstByOrderByWidth();
}
