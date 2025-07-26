package io.github.eirikh1996.nationcraft.api.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.UUID;

public class Compass {
    private final Direction direction;
    private final UUID id;
    private final TextComponent[] lines;
    public Compass(Direction direction) {
        id = UUID.randomUUID();
        this.direction = direction;
        lines = new TextComponent[3];
        lines[0] = Component.text()
                .append(Component.text("\\ ", direction == Direction.NORTH_WEST ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("North-West"))))
                .append(Component.text("N", direction == Direction.NORTH ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("North"))))
                .append(Component.text("/ ", direction == Direction.NORTH_EAST ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("North-East"))))
                .build();
        lines[1] = Component.text()
                .append(Component.text("W ", direction == Direction.WEST ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("West"))))
                .append(Component.text("O ", NamedTextColor.YELLOW))
                .append(Component.text("E ", direction == Direction.EAST ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("East"))))
                .build();
        lines[2] = Component.text()
                .append(Component.text("/ ", direction == Direction.SOUTH_WEST ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("South-West"))))
                .append(Component.text("N", direction == Direction.SOUTH ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("South"))))
                .append(Component.text("\\ ", direction == Direction.SOUTH_EAST ? NamedTextColor.DARK_RED : NamedTextColor.YELLOW).hoverEvent(HoverEvent.showText(Component.text("South-East"))))
                .build();
    }

    public TextComponent[] getLines() {
        return lines;
    }

    public TextComponent getLine(int index){
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
