package io.github.eirikh1996.nationcraft.utils;

import org.bukkit.Bukkit;

import java.text.DecimalFormat;

public enum Direction {
    NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

    public static Direction fromYaw(float yaw){
        float rotation;
        if (yaw < 0){
            rotation = 360f + yaw;
        } else {
            rotation = yaw;
        }
        if (rotation > 337.5 || rotation <= 22.5){
            return SOUTH;
        } else if (rotation <= 67.5){
            return SOUTH_WEST;
        } else if (rotation <= 112.5){
            return WEST;
        } else if (rotation <= 157.5){
            return NORTH_WEST;
        } else if (rotation <= 202.5){
            return NORTH;
        } else if (rotation <= 247.5){
            return NORTH_EAST;
        } else if (rotation <= 292.5){
            return EAST;
        } else {
            return SOUTH_EAST;
        }
    }
}
