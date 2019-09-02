package io.github.eirikh1996.nationcraft.utils;

public enum Direction {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    public static Direction fromYaw(float yaw){
        if (yaw < -157.4f){
            return NORTH;
        } else if (yaw < -112.4f){
            return NORTH_EAST;
        } else if (yaw < -67.4f){
            return EAST;
        } else if (yaw < -22.4f){
            return SOUTH_EAST;
        } else if (yaw < 22.4f){
            return SOUTH;
        } else if (yaw < 67.4f){
            return SOUTH_WEST;
        } else if (yaw < 112.4f){
            return WEST;
        } else if (yaw < 157.4f){
            return NORTH_WEST;
        }
        return NORTH;
    }
}
