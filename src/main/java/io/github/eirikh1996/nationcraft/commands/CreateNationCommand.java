package io.github.eirikh1996.nationcraft.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;

public class CreateNationCommand implements CommandExecutor {
	private final NationCraft plugin;
	
	public CreateNationCommand(NationCraft plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (cmd.getName().equalsIgnoreCase("createnation")) {
			if (sender instanceof Player) {
				if (!sender.hasPermission("nationcraft.command.createnation")) {
					sender.sendMessage("Error: You do not have permission to use this command!");
				} else {
					String nationName = args[0];
					if (nationName.length() >= 1) {
						File file = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations/" + nationName + ".nation");
						if (file.mkdirs() && !file.exists()) {
							try {
								PrintWriter writer = new PrintWriter(nationName + ".nation");
								writer.println("name: " + nationName);
								writer.println("description: Default description.");
								writer.println("allies:");
								writer.println("enemies:");
								writer.println("members:");
								writer.println("- " + ((Player) sender).getUniqueId() + "/" );
								writer.close();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			} else {
				sender.sendMessage("Error: You must be a player to execute this command!");
			}
		}
		
		return false;
	}
	
}
