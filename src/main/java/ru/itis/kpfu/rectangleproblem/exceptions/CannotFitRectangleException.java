package ru.itis.kpfu.rectangleproblem.exceptions;

/**
 * @author Zagir Dingizbaev
 */
public class CannotFitRectangleException extends Exception{

    private final Long step;

    public CannotFitRectangleException(Long step) {
        this.step = step;
    }

    @Override
    public String getMessage() {
        return "Couldn't find any box that fits new rectangle. Step = " + step;
    }
}
