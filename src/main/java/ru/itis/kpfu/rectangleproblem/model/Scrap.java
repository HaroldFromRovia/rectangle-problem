package ru.itis.kpfu.rectangleproblem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(indexes = {@Index(columnList = "height"), @Index(columnList = "processed")})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Scrap extends RectangularWithPolygon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "scrap", orphanRemoval = true, targetEntity = Rectangle.class)
    @JsonManagedReference("rectangles")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Rectangle> rectangles;

    @Enumerated(EnumType.STRING)
    private Orientation orientation;

    private boolean isEndFace;
    private boolean isRectangle;
    private boolean processed;
}
