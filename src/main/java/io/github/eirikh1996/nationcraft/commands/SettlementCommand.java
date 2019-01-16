package io.github.eirikh1996.nationcraft.commands;

import io.github.eirikh1996.nationcraft.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettlementCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("settlement")){
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(Messages.ERROR + Messages.MUST_BE_PLAYER);
            return true;
        }
        if (!commandSender.hasPermission("nationcraft.settlement")){
            commandSender.sendMessage(Messages.ERROR + Messages.NO_PERMISSION);
            return true;
        }
        if (strings.length == 0){
            return true;
        }
        else if (strings[0].equalsIgnoreCase("create")){

        }
        return false;
    }
}
