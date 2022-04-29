package ru.itis.kpfu.rectangleproblem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Zagir Dingizbaev
 */

@Data
@Builder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class Rectangular {
    @Column(precision = 18, scale = 16, columnDefinition="Decimal(18,16)")
    private Double height;
    @Column(precision = 18, scale = 16, columnDefinition = "Decimal(18,16)")
    private Double width;
}
