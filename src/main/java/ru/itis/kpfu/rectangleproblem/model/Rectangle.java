package ru.itis.kpfu.rectangleproblem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.locationtech.jts.geom.Point;

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

    @Column(columnDefinition = "Point")
    private Point bottomLeft;
    @Column(columnDefinition = "Point")
    private Point upperRight;

    @ManyToOne
    @JoinColumn(name = "scrap_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference("rectangles")
    private Scrap scrap;
}
