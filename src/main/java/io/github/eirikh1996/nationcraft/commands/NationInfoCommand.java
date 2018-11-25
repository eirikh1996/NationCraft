package io.github.eirikh1996.nationcraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;

public class NationInfoCommand implements CommandExecutor {
	private NationCraft plugin;

	public NationInfoCommand(NationCraft plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if (cmd.getName().equalsIgnoreCase("nationinfo")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Error: you must be player to execute this command");
			}
			if (!sender.hasPermission("nationcraft.nationinfo")) {
				sender.sendMessage("Error: you do not have permission to execute this command");
			}
			if (args.length == 0) {
				return false;
			}
			else {
				Player p = (Player) sender;
				String nationName;
				Nation n = new Nation();
				
				if (n.isOwnNation(p)) {
					nationName = n.getName();
				} else {
					nationName = args[0];
				}
				boolean ally = n.isAlliedWith(nationName);
				boolean enemy = n.isAtWarWith(nationName);
				Messages m = null;
				m.nationInfo(nationName, p, ally, enemy);
			}
		}
		return false;
	}
	

}
