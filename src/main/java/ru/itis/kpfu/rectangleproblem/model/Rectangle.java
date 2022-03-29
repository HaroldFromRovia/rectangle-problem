package ru.itis.kpfu.rectangleproblem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Rectangle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long index;
    private Double height;
    private Double width;

    private Polygon figure;

    @ManyToOne
    @JoinColumn(name = "scrap_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference("rectangles")
    private Scrap scrap;
}
