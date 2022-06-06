package ru.itis.kpfu.rectangleproblem.exceptions;

/**
 * @author Zagir Dingizbaev
 */


public class BoxNotFoundException extends RuntimeException{

    private final Long step;

    public BoxNotFoundException(Long step) {
        this.step = step;
    }

    @Override
    public String getMessage() {
        return "Couldn't find any box that fits new rectangle. Step = " + step;
    }
}
