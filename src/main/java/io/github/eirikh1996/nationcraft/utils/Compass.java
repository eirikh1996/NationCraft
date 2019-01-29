package io.github.eirikh1996.nationcraft.utils;

public class Compass {
    public enum Direction {
        SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NORTH, NORTH_EAST, EAST, SOUTH_EAST
    }
    public static Direction getDirection(double degrees){
        if (degrees < 0){
            degrees += 360.0;
        }

        if (degrees < 22.5 && degrees >= 337.5)
            return Direction.SOUTH;
        if (degrees < 67.5 && degrees >= 22.5)
            return Direction.SOUTH_WEST;
        if (degrees < 112.5 && degrees >= 67.5)
            return Direction.WEST;
        if (degrees < 157.5 && degrees >= 112.5)
            return Direction.NORTH_WEST;
        if (degrees < 202.5 && degrees >= 157.5)
            return Direction.NORTH;
        if (degrees < 247.5 && degrees >= 202.5)
            return Direction.NORTH_EAST;
        if (degrees < 292.5 && degrees >= 247.5)
            return Direction.EAST;
        if (degrees < 337.5 && degrees >= 292.5)
            return Direction.SOUTH_EAST;
        else
            return null;

    }
}
