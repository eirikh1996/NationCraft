package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class ChatModeCommand extends Command {

    ChatModeCommand() {
        super("chatmode");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (Settings.UseExternalChatPlugin) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "External chat plugin is being used");
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        if (args.length == 0){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Usage: /chatmode <global|g|ally|a|truce|t|nation|n|settlement|s>. Current chat mode: " + player.getChatMode().name().toLowerCase());
            return;
        }
        ChatMode cMode = ChatMode.getChatMode(args[0].toUpperCase());
        if (cMode == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + args[0] + " is not a chat mode!");
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + cMode.name().toLowerCase() + " chatmode set");
        player.setChatMode(cMode);
        return;
    }

    @Override
    public List<String> getTabCompletions(NCCommandSender sender, String[] args) {
        return ChatMode.getNames();
    }
}
