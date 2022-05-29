package ru.itis.kpfu.rectangleproblem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zagir Dingizbaev
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Polygon {

    private Coordinate[] coordinates;
}
