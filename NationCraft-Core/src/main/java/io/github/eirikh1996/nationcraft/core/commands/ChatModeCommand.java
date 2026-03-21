package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import io.github.eirikh1996.nationcraft.core.commands.parameters.ChatModeParameterType;
import net.kyori.adventure.text.Component;

import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class ChatModeCommand extends Command {

    ChatModeCommand() {
        super("chatmode");
        addParameter("chatmode", new ChatModeParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        if (Settings.UseExternalChatPlugin) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("External chat plugin is being used")));
            return;
        }
        ChatMode cMode = getParameter("chatmode").getValue();
        if (cMode == null){
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text(cMode.name().toLowerCase() + " chatmode set")));
        player.setChatMode(cMode);
    }

    @Override
    public List<String> getTabCompletions(NCCommandSender sender, String[] args) {
        return ChatMode.getNames();
    }
}
