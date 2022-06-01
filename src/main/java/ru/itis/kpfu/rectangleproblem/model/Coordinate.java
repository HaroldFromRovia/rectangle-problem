package ru.itis.kpfu.rectangleproblem.model;

import lombok.*;

/**
 * @author Zagir Dingizbaev
 */

@Getter
@Setter
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

    @Override
    public String toString() {
        return x + "," + y;
    }
}
