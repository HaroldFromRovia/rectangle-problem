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
public class Point {

    private Double x;
    private Double y;

    public Coordinate getCoordinate() {
        return new Coordinate(x,y);
    }
}
