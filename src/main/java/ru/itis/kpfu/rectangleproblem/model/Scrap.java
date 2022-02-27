package ru.itis.kpfu.rectangleproblem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Point")
    private Point bottomLeft;
    @Column(columnDefinition = "Point")
    private Point upperRight;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scrap", orphanRemoval = true, targetEntity = Rectangle.class)
    @JsonManagedReference("rectangles")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Rectangle> rectangles;
}
