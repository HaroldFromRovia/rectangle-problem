package ru.itis.kpfu.rectangleproblem.service.interfaces;

import ru.itis.kpfu.rectangleproblem.model.LRP;
import ru.itis.kpfu.rectangleproblem.model.Rectangle;

public interface LRPService {

    LRP cropLRP(Rectangle rectangle);
    LRP initLRP();
    LRP getCurrent();
}
