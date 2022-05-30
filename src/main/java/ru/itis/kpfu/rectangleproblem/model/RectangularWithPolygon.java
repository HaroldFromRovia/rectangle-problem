package ru.itis.kpfu.rectangleproblem.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.itis.kpfu.rectangleproblem.converter.JsonConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

/**
 * @author Zagir Dingizbaev
 */

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class RectangularWithPolygon extends Rectangular {

    @Type(type = "jsonb")
    @Column(name = "figure", columnDefinition = "jsonb")
    private Polygon figure;
}
