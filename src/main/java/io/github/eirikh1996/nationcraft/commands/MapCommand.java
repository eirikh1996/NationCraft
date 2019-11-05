package io.github.eirikh1996.nationcraft.commands;

import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MapCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        long startTime = System.currentTimeMillis();
        if (!command.getName().equalsIgnoreCase("map")){
            return false;
        }
        if (!(commandSender instanceof Player)){
            commandSender.sendMessage(Messages.ERROR + Messages.MUST_BE_PLAYER);
            return true;
        }
        Player p = (Player) commandSender;
        if (strings.length == 0){
            Messages.generateTerritoryMap(p);
        } else if (strings[0].equalsIgnoreCase("auto")){
            @NotNull String status = "";;
            final NCPlayer player = PlayerManager.getInstance().getPlayer(p.getUniqueId());
            if (!player.isAutoUpdateTerritoryMap()){
                player.setAutoUpdateTerritoryMap(true);
                status = "enabled";
            } else {
                player.setAutoUpdateTerritoryMap(false);
                status = "disabled";
            }
            p.sendMessage("Automatic map updates " + status);
        }
        long endTime = System.currentTimeMillis();
        if (Settings.Debug){
            Bukkit.broadcastMessage("Command processing took (ms): " + (endTime - startTime));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> completions = new ArrayList<>();
        if (commandSender.hasPermission("nationcraft.map")){
            return Collections.emptyList();
        } else {
            completions.add("auto");
        }
        List<String> returnValues = new ArrayList<>();
        for (String completion : completions){
            returnValues.add(completion);
        }
        return returnValues;
    }
}
