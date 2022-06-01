package ru.itis.kpfu.rectangleproblem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zagir Dingizbaev
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Polygon {

    private List<Coordinate> coordinates;
}
