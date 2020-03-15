package io.github.eirikh1996.nationcraft.api.utils;

import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.HoverEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;

import java.util.UUID;

public class Compass {
    private final Direction direction;
    private final UUID id;
    private final ChatText[] lines;
    public Compass(Direction direction) {
        id = UUID.randomUUID();
        this.direction = direction;
        lines = new ChatText[3];
        switch (direction){
            case NORTH:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.DARK_RED, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case NORTH_EAST:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.DARK_RED, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case EAST:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.DARK_RED, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case SOUTH_EAST:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.DARK_RED, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case SOUTH:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.DARK_RED, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case SOUTH_WEST:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.DARK_RED, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case WEST:
                lines[0] = ChatText.builder()
                        .addText(TextColor.YELLOW, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.DARK_RED, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
            case NORTH_WEST:
                lines[0] = ChatText.builder()
                        .addText(TextColor.DARK_RED, "\\ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-West"))
                        .addText(TextColor.YELLOW, "N", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North"))
                        .addText(TextColor.YELLOW, " /" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "North-East"))
                        .build();
                lines[1] = ChatText.builder()
                        .addText(TextColor.YELLOW, "W ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "West"))
                        .addText(TextColor.YELLOW, "O")
                        .addText(TextColor.YELLOW, " E" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "East"))
                        .build();
                lines[2] = ChatText.builder()
                        .addText(TextColor.YELLOW, "/ ", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-West"))
                        .addText(TextColor.YELLOW, "S", new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South"))
                        .addText(TextColor.YELLOW, " \\" + TextColor.RESET, new HoverEvent(HoverEvent.Action.SHOW_TEXT, "South-East"))
                        .build();
                break;
        }
    }

    public ChatText[] getLines() {
        return lines;
    }

    public ChatText getLine(int index){
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
