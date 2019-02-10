package io.github.eirikh1996.nationcraft.claiming;

import io.github.eirikh1996.nationcraft.exception.InvalidShapeException;

import java.util.*;

public enum Shape {
    LINE, CIRCLE, SQUARE, SINGLE, ALL;

    private static Map<String, Shape> FROM_STRING = new HashMap<>();

    static {
        for (Shape shape : Shape.values()){
            FROM_STRING.put(shape.toString().toLowerCase(), shape);
            if (shape.equals(SQUARE))
                FROM_STRING.put(shape.toString().toLowerCase().substring(0,1), shape);
            else
                FROM_STRING.put(shape.toString().toLowerCase().substring(0,0), shape);
        }
    }

    public static Shape getShape(String name){
        if (!FROM_STRING.containsKey(name.toLowerCase())){
            throw new InvalidShapeException( name.toLowerCase() + " is not a valid shape!");
        }
        return FROM_STRING.get(name);
    }
    public static String[] getShapeNames(){
        Set<String> strings = FROM_STRING.keySet();
        String[] returnStrings = new String[strings.size()];
        int index = 0;
        for (String s : strings){
            returnStrings[index++] = s;
        }
        return returnStrings;
    }
}
