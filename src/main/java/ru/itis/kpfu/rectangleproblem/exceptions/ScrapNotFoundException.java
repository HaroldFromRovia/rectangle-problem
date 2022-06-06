package ru.itis.kpfu.rectangleproblem.exceptions;

/**
 * @author Zagir Dingizbaev
 */


public class ScrapNotFoundException extends RuntimeException{

    private final Long step;

    public ScrapNotFoundException(Long step) {
        this.step = step;
    }

    @Override
    public String getMessage() {
        return "Couldn't find any scrap that fits new rectangle. Step = " + step;
    }
}
