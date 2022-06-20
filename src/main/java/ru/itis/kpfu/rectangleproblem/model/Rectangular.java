package ru.itis.kpfu.rectangleproblem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

/**
 * @author Zagir Dingizbaev
 */

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class Rectangular {
    private Double height;
    private Double width;
}
