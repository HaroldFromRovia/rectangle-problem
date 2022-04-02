package ru.itis.kpfu.rectangleproblem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author Zagir Dingizbaev
 */
@Component
@RequiredArgsConstructor
public class ShutdownManager {

    private final ApplicationContext appContext;

    public void initiateShutdown(int returnCode) {
        SpringApplication.exit(appContext, () -> returnCode);
    }
}
