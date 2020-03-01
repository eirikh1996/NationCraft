package io.github.eirikh1996.nationcraft.core.utils;

import io.github.eirikh1996.nationcraft.api.objects.TextColor;

import java.util.UUID;

public class Compass {
    private final Direction direction;
    private final UUID id;
    private final String[] lines;
    public Compass(Direction direction) {
        id = UUID.randomUUID();
        this.direction = direction;
        lines = new String[3];
        switch (direction){
            case NORTH:
                lines[0] = TextColor.YELLOW + "\\ " + TextColor.DARK_RED + "N" + TextColor.YELLOW + " /" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ S \\" + TextColor.RESET;
                break;
            case NORTH_EAST:
                lines[0] = TextColor.YELLOW + "\\ N " + TextColor.DARK_RED + "/" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ S \\" + TextColor.RESET;
                break;
            case EAST:
                lines[0] = TextColor.YELLOW + "\\ N /" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O " + TextColor.DARK_RED + "E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ S \\" + TextColor.RESET;
                break;
            case SOUTH_EAST:
                lines[0] = TextColor.YELLOW + "\\ N /" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ S " + TextColor.DARK_RED + "\\" + TextColor.RESET;
                break;
            case SOUTH:
                lines[0] = TextColor.YELLOW + "\\ N /" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ " + TextColor.DARK_RED + "S" + TextColor.YELLOW + " \\" + TextColor.RESET;
                break;
            case SOUTH_WEST:
                lines[0] = TextColor.YELLOW + "\\ N /" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
                lines[2] = TextColor.DARK_RED + "/" + TextColor.YELLOW + " S \\" + TextColor.RESET;
                break;
            case WEST:
                lines[0] = TextColor.YELLOW + "\\ N /" + TextColor.RESET;
                lines[1] = TextColor.DARK_RED + "W" + TextColor.YELLOW + " O E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ S \\" + TextColor.RESET;
                break;
            case NORTH_WEST:
                lines[0] = TextColor.DARK_RED + "\\" + TextColor.YELLOW +" N /" + TextColor.RESET;
                lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
                lines[2] = TextColor.YELLOW + "/ S \\" + TextColor.RESET;
                break;
        }
    }

    public String[] getLines() {
        return lines;
    }

    public String getLine(int index){
        return lines[index];
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /*
    lines[0] = TextColor.YELLOW + "\\ N /" + TextColor.RESET;
    lines[1] = TextColor.YELLOW + "W O E" + TextColor.RESET;
    lines[2] = TextColor.YELLOW + "/ S \\" + TextColor.RESET;
 */


}
