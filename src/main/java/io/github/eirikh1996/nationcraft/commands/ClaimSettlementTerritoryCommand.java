package io.github.eirikh1996.nationcraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.settlement.Settlement;

public class ClaimSettlementTerritoryCommand implements CommandExecutor {
	private NationCraft plugin;
	public ClaimSettlementTerritoryCommand(NationCraft plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if (command.getName().equalsIgnoreCase("claimsettlement")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Messages.ERROR + Messages.MUST_BE_PLAYER);				
			}
			
			if (!sender.hasPermission("nationcraft.settlement.create")) {
				
			} else {
				if (args.length < 1) {
					
				} else {
					String shape;
					int radius;
					Player p = (Player) sender;
					World w = p.getWorld();
					Settlement s = new Settlement();
					List<Nation> nationList = new ArrayList<>();
					Nation n = null;
					if (args[1] != null) {
						radius = Integer.getInteger(args[1]);
					} else {
						radius = 0;
					}
					if (args[0] != null) {
						shape = args[0];
					} else {
						shape = "";
					}
					if (shape.equalsIgnoreCase("circle")||args[0].equalsIgnoreCase("c")) {
						int negativeRadius = -radius;
						int squaredRadius = radius * radius;
						for (int dx = negativeRadius; dx <= radius ; dx++) {
							for (int dz = negativeRadius; dz <= radius ; dz++) {
								if (dx*dx + dz*dz > squaredRadius) continue;
								
								int x = p.getLocation().getChunk().getX() + dx;
								int z = p.getLocation().getChunk().getZ() + dz;
								Chunk territory = w.getChunkAt(x, z);
								if (n.hasPlayer(p) && !n.isNationTerritory(territory)) {
									sender.sendMessage(Messages.ERROR + "Settlement territory must be supported by your nation's territory");
									break;
								}
								
								s.addTerritory(territory); 
							}
						}
					} else if (shape.equalsIgnoreCase("line")||args[0].equalsIgnoreCase("l")) {
						
					} else if (shape.equalsIgnoreCase("square")||args[0].equalsIgnoreCase("s")) {
						for (int i = -radius; i <= radius ; i++) {
							int chunkDX = w.getChunkAt(p.getLocation()).getX() + i;
							int chunkDZ = w.getChunkAt(p.getLocation()).getZ() + i;
							Chunk territory = w.getChunkAt(chunkDX, chunkDZ);
							s.addTerritory(territory);
						}
					} else {
						
					}
				}
			}
			
		}
		return false;
	}

}
