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
public class Coordinate {
    private double x;
    private double y;

    public Double distance(Coordinate coordinate) {
        double dx = x - coordinate.x;
        double dy = y - coordinate.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
