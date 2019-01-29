package io.github.eirikh1996.nationcraft.claiming;

import java.util.HashMap;
import java.util.Map;

public enum Shape {
    LINE, CIRCLE, SQUARE;

    private static Map<String, Shape> FROM_STRING = new HashMap<>();

    static {
        for (Shape shape : Shape.values()){
            FROM_STRING.put(shape.toString(), shape);
        }
    }

    public static Shape getShape(String name){
        return FROM_STRING.get(name);
    }
}
