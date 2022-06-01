package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;
import ru.itis.kpfu.rectangleproblem.converter.PolygonConverter;

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
public class RectangularWithPolygon extends Rectangular {

    @Convert(converter = PolygonConverter.class)
    private Polygon figure;
}
