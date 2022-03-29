package ru.itis.kpfu.rectangleproblem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.*;
import java.util.List;

@Data
@Table(indexes = @Index(columnList = "width"))
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Polygon figure;

    private Double width;
    private Double height;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "scrap", orphanRemoval = true, targetEntity = Rectangle.class)
    @JsonManagedReference("rectangles")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Rectangle> rectangles;
}
