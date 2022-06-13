package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(indexes = {@Index(columnList = "height"), @Index(columnList = "width"), @Index(columnList = "processed")})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Scrap extends RectangularWithPolygon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Orientation orientation;

    private boolean isEndFace;
    private boolean isRectangle;
    private boolean processed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Scrap scrap = (Scrap) o;
        return id != null && Objects.equals(id, scrap.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
