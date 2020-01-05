package io.github.eirikh1996.nationcraft.commands;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;

import static io.github.eirikh1996.nationcraft.messages.Messages.*;

public class NationCraftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("nationcraft")){
            return false;
        }
        if (strings.length == 0){
            defaultCommand(commandSender);
            return true;
        }
        if (strings[0].equalsIgnoreCase("convertfromfactions")){
            File serverDir = NationCraft.getInstance().getServer().getWorldContainer();

        }

        if (strings[0].equalsIgnoreCase("player")){
            Player other;
            try {
                other = Bukkit.getPlayer(strings[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                other = null;
            }
            playerSubCommand(commandSender, other);
        }
        return true;
    }

    private void playerSubCommand(CommandSender sender, Player player){
        if (player == null && !(sender instanceof Player)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a player name");
            return;
        }
        if (player == null) {
            player = (Player) sender;
        }
        Messages.displayPlayerInfo(player);
    }

    private void defaultCommand(CommandSender sender){
        PluginDescriptionFile desc = NationCraft.getInstance().getDescription();
        sender.sendMessage(ChatColor.DARK_AQUA + "=====================[" + ChatColor.AQUA + "Nation" + ChatColor.GRAY + "Craft" + ChatColor.DARK_AQUA + "]=====================");
        sender.sendMessage("author(s): " + desc.getDescription() + ", version: " + desc.getVersion());
        sender.sendMessage("/nation help for nation commands");
        sender.sendMessage("/settlement help for settlement commands");
    }
}
