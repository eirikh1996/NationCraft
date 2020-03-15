package io.github.eirikh1996.nationcraft.api.utils;

import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;

import java.util.HashSet;
import java.util.Set;

public class JSONUtils {
    public static final Set<JSONCommand> jsonCommands = new HashSet<>();

    public static String getJSON(final Runnable clickEvent, String command, String displayMessage, String hoverMessage) {

        final JSONCommand jsonCommand = new JSONCommand(command, clickEvent);

        jsonCommands.add(jsonCommand);

        return "{\"text\":\"" + displayMessage + TextColor.RESET + "\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + jsonCommand.getCommand() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverMessage + TextColor.RESET +  "}}";
    }
}
