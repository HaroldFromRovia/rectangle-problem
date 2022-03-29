package ru.itis.kpfu.rectangleproblem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.itis.kpfu.rectangleproblem.config.AlgorithmProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = AlgorithmProperties.class)
public class RectangleProblemApplication{

    public static void main(String[] args) {
        SpringApplication.run(RectangleProblemApplication.class, args);
    }
}
