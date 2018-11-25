package io.github.eirikh1996.nationcraft.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.settlement.Settlement;

public class CreateSettlementCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if (command.getName().equalsIgnoreCase("createsettlement")) {
			if (!(sender instanceof Player)) {
				
			} 
			
			if (!sender.hasPermission("nationcraft.settlement.create")) {
				
			}
			
			if (args[0].length() < 3) {
				sender.sendMessage(Messages.ERROR + "Settlement name must be at least 3 characters long.");
			}
			else {
				Player p = (Player) sender;
				List<Player> pList = new ArrayList<>();
				pList.add(p);
				Settlement settlement = new Settlement(args[0], null, null, pList);
				File settlementFile = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/settlements/" + args[0] + ".settlement");
				settlement.saveToFile(settlementFile);
			}
		}
		return false;
	}

}
