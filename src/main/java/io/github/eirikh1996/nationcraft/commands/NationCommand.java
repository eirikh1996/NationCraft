package io.github.eirikh1996.nationcraft.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;

public class NationCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (cmd.getName().equalsIgnoreCase("nation")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Error: You must be a player to execute this command!");
				return true;
			}
			if (args[0].equalsIgnoreCase("create")){
				createNation(sender, args[1]);
				return true;
			}
			if (args[0].equalsIgnoreCase("ally")){

			}
			if (args[0].equalsIgnoreCase("neutral")){

			}
			if (args[0].equalsIgnoreCase("war")){

			}
			if (args[0].equalsIgnoreCase("disband")){

			}
			if (args[0].equalsIgnoreCase("ally")){

			}
		}
		
		return false;
	}

	private void createNation(CommandSender sender, String name){
		if (!sender.hasPermission("nationcraft.command.createnation")) {
			sender.sendMessage("Error: You do not have permission to use this command!");
			return;
		}
		if (name.length() >= 1) {
			File file = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations/" + name + ".nation");
			if (file.mkdirs() && !file.exists()) {
				try {
					PrintWriter writer = new PrintWriter(name + ".nation");

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
}
