package io.github.eirikh1996.nationcraft.utils;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;

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
                lines[0] = ChatColor.YELLOW + "\\ " + ChatColor.DARK_RED + "N" + ChatColor.YELLOW + " /" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ S \\" + ChatColor.RESET;
                break;
            case NORTH_EAST:
                lines[0] = ChatColor.YELLOW + "\\ N " + ChatColor.DARK_RED + "/" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ S \\" + ChatColor.RESET;
                break;
            case EAST:
                lines[0] = ChatColor.YELLOW + "\\ N /" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O " + ChatColor.DARK_RED + "E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ S \\" + ChatColor.RESET;
                break;
            case SOUTH_EAST:
                lines[0] = ChatColor.YELLOW + "\\ N /" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ S " + ChatColor.DARK_RED + "\\" + ChatColor.RESET;
                break;
            case SOUTH:
                lines[0] = ChatColor.YELLOW + "\\ N /" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ " + ChatColor.DARK_RED + "S" + ChatColor.YELLOW + " \\" + ChatColor.RESET;
                break;
            case SOUTH_WEST:
                lines[0] = ChatColor.YELLOW + "\\ N /" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
                lines[2] = ChatColor.DARK_RED + "/ " + ChatColor.YELLOW + " S \\" + ChatColor.RESET;
                break;
            case WEST:
                lines[0] = ChatColor.YELLOW + "\\ N /" + ChatColor.RESET;
                lines[1] = ChatColor.DARK_RED + "W " + ChatColor.YELLOW + " O E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ S \\" + ChatColor.RESET;
                break;
            case NORTH_WEST:
                lines[0] = ChatColor.DARK_RED + "\\" + ChatColor.YELLOW +" N /" + ChatColor.RESET;
                lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
                lines[2] = ChatColor.YELLOW + "/ S \\" + ChatColor.RESET;
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
    lines[0] = ChatColor.YELLOW + "\\ N /" + ChatColor.RESET;
    lines[1] = ChatColor.YELLOW + "W O E" + ChatColor.RESET;
    lines[2] = ChatColor.YELLOW + "/ S \\" + ChatColor.RESET;
 */


}
