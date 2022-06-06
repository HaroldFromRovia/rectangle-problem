package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.itis.kpfu.rectangleproblem.model.enumerated.Orientation;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * @author Zagir Dingizbaev
 */

@Getter
@Setter
@ToString
@Table(indexes = {@Index(columnList = "height"), @Index(columnList = "width")})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Orientation orientation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Box box = (Box) o;
        return id != null && Objects.equals(id, box.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
