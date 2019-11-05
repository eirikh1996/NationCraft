package io.github.eirikh1996.nationcraft.commands;

import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.github.eirikh1996.nationcraft.messages.Messages.*;

public class ChatModeCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!command.getName().equalsIgnoreCase("ChatMode")){
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return true;
        }
        final NCPlayer player = PlayerManager.getInstance().getPlayer(((Player) commandSender).getUniqueId());
        if (strings.length == 0){
            commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Usage: /chatmode <global|g|ally|a|truce|t|nation|n|settlement|s>. Current chat mode: " + player.getChatMode().name().toLowerCase());
            return true;
        }
        ChatMode cMode = ChatMode.getChatMode(strings[0].toUpperCase());
        if (cMode == null){
            commandSender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + strings[0] + " is not a chat mode!");
            return true;
        }
        player.setChatMode(cMode);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ArrayList<String> subCmds = new ArrayList<>();
        for (ChatMode chatMode : ChatMode.values()){
            subCmds.add(chatMode.name().toLowerCase());
        }
        if (strings.length == 0){
            return subCmds;
        }
        ArrayList<String> completions = new ArrayList<>();
        for (String str : subCmds){
            if (!str.startsWith(strings[0])){
                continue;
            }
            completions.add(str);
        }
        return completions;

    }
}
