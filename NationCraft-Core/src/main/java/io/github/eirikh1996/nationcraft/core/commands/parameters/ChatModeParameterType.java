package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class ChatModeParameterType extends AbstractParameterType<ChatMode> {
    public ChatModeParameterType() {
        super(ChatMode.class);
    }

    @Override
    public ChatMode readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty() && sender instanceof NCPlayer player) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Usage: /chatmode <global|g|ally|a|truce|t|nation|n|settlement|s>. Current chat mode: " + player.getChatMode().name().toLowerCase())));
            return null;
        }
        ChatMode mode = ChatMode.getChatMode(input.toUpperCase());
        if (mode == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Invalid chat mode: " + input)));
        }
        return mode;
    }
}
