package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Table(indexes = @Index(columnList = "step"))
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LRP extends Rectangular{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long step;
    private Long rectangleIndex;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LRP lrp = (LRP) o;
        return id != null && Objects.equals(id, lrp.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
