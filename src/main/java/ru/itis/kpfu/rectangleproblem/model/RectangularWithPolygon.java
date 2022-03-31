package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.MappedSuperclass;

/**
 * @author Zagir Dingizbaev
 */

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class RectangularWithPolygon extends Rectangular {

    private Polygon figure;
}
