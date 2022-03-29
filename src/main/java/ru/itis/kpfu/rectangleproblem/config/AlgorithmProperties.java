package ru.itis.kpfu.rectangleproblem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Zagir Dingizbaev
 */

@Data
@ConfigurationProperties(prefix = "algorithm")
public class AlgorithmProperties {

    private Long upperBound;
    private Double power;
    private Double size;
}
