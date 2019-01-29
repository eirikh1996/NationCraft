package io.github.eirikh1996.nationcraft.messages;

import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.utils.Compass;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.Ranks;

public class Messages {
	public static String ERROR = ChatColor.DARK_RED + "Error: ";
	public static String WARNING = ChatColor.YELLOW + "Warning: ";
	public static String MUST_BE_PLAYER = "You must be player to execute this command!";
	public static String NO_PERMISSION = "You don't have permission!";
	public static void nationInfo(Nation n, Player p, ChatColor color) {
		String name = n.getName();
		String description = n.getDescription();
		List<String> allyList = n.getAllies();
		List<String> enemyList = n.getEnemies();
		Map<UUID, Ranks> playerList = n.getPlayers();
		String onlinePlayers = "";
		String offlinePlayers = "";

			for (UUID id : playerList.keySet()) {

					if (Bukkit.getPlayer(id) != null) {
						Player player = Bukkit.getPlayer(id);
						String playerName = player.getName();
						Ranks r = playerList.get(id);
						String rankMarker = "";
						if (r == Ranks.RECRUIT) {
							rankMarker = "-";
						}
						if (r == Ranks.MEMBER) {
							rankMarker = "+";
						}
						if (r == Ranks.OFFICER) {
							rankMarker = "*";
						}
						if (r == Ranks.OFFICIAL) {
							rankMarker = "**";
						}
						if (r == Ranks.LEADER) {
							rankMarker = "***";
						}
						onlinePlayers += rankMarker;
						onlinePlayers += playerName;
						if (playerList.keySet().size() > 1){
							onlinePlayers += ", ";
						}

					} else {
						OfflinePlayer player = Bukkit.getOfflinePlayer(id);
						String playerName = player.getName();
						Ranks r = playerList.get(player.getUniqueId());
						String rankMarker = "";
						if (r == Ranks.RECRUIT) {
							rankMarker = "-";
						}
						if (r == Ranks.MEMBER) {
							rankMarker = "+";
						}
						if (r == Ranks.OFFICER) {
							rankMarker = "*";
						}
						if (r == Ranks.OFFICIAL) {
							rankMarker = "**";
						}
						if (r == Ranks.LEADER) {
							rankMarker = "***";
						}
						offlinePlayers += rankMarker;
						offlinePlayers += playerName;
						if (playerList.keySet().size() > 1){
							offlinePlayers += ", ";
						}
					}
				}
			p.sendMessage(ChatColor.YELLOW + "------------------{ Nation:" + color + " " + name + ChatColor.YELLOW +  " }-----------------");
			p.sendMessage(ChatColor.YELLOW + "Description: " + color + description);
			p.sendMessage(ChatColor.YELLOW + "Territory: " + n.getTerritory().size());
			p.sendMessage(ChatColor.YELLOW + "Strength: " + (n.getStrength() >= n.getTerritory().size() ? ChatColor.GREEN : ChatColor.RED) + n.getStrength());
			p.sendMessage(ChatColor.YELLOW + "Maximum strength: " + n.getMaxStrength());
			p.sendMessage(ChatColor.YELLOW + "Allies: " + ChatColor.DARK_PURPLE + (allyList != null ? allyList.toString() : ""));
			p.sendMessage(ChatColor.YELLOW + "Enemies: " + ChatColor.RED + (enemyList != null ? enemyList.toString() : ""));
			p.sendMessage(ChatColor.YELLOW + "Players online: " + color + onlinePlayers);
			p.sendMessage(ChatColor.YELLOW + "Players offline: " + color + offlinePlayers);
		}
	public static void generateTerritoryMap(Player p){
		Chunk chunk = p.getLocation().getChunk();
		int minX = chunk.getX() - 20;
		int maxX = chunk.getX() + 20;
		int minZ = chunk.getZ() - 10;
		int maxZ = chunk.getZ() + 10;
		Map<Nation, String> nationMarkers = nationMarkers(20, p);
		p.sendMessage(ChatColor.YELLOW + "======{" + ChatColor.GREEN + "Nation map" + ChatColor.YELLOW + "}======");
		for (int z = minZ ; z <= maxZ ; z++){
			String mapLine = "";
			String compass = "";
			int line = z - minZ;
			if (line <= 2) {
				compass = generateCompass(line, p.getLocation().getYaw()) + " ";
			}
			for (int x = minX ; x <= maxX ; x++){
				int column = x - minX;
				Chunk testChunk = p.getWorld().getChunkAt(x,z);

				mapLine += getTerritoryMarker(nationMarkers, testChunk, p);

			}
			if (compass.length() > 0){
				mapLine = mapLine.substring(5, 41);
			}
			NationCraft.getInstance().getLogger().info(String.valueOf(compass.length()));
			p.sendMessage(compass + mapLine);
		}
		p.sendMessage(ChatColor.YELLOW + "==============================================");
	}

	private static Map<Nation, String> nationMarkers( int scanRange, Player p){
		String[] markerArray = new String[]{"/", "\\", "#", "%","+","?","$","¤","@","&","*","£","<", ">"};
		TreeSet<String> markers = new TreeSet<>();
		for (String marker : markerArray){
			markers.add(marker);
		}
		Map<Nation, String> returnMap = new HashMap<>();
		Chunk chunk = p.getLocation().getChunk();
		int minX = chunk.getX() - scanRange;
		int maxX = chunk.getX() + scanRange;
		int minZ = chunk.getZ() - scanRange;
		int maxZ = chunk.getZ() + scanRange;
		for (int z = minZ ; z <= maxZ ; z++) {
			for (int x = minX; x <= maxX; x++) {
				Chunk foundChunk = p.getWorld().getChunkAt(x,z);
				Nation foundNation = NationManager.getInstance().getNationAt(foundChunk);
				if (!returnMap.containsKey(foundNation)){
					returnMap.put(foundNation, markers.first());
				}
			}
		}
		return returnMap;
	}
	private static String getTerritoryMarker(Map<Nation, String> nationMarkers , Chunk territory, Player player){

		String marker = "-";

		Nation n = NationManager.getInstance().getNationAt(territory);
		if (n != null){
			marker = NationManager.getInstance().getColor(player,n) + nationMarkers.get(n);
		} else if (territory == player.getLocation().getChunk()){
			marker = ChatColor.BLUE + "+" + ChatColor.RESET;
		}
		return marker;
	}
	private static String generateCompass(int line, float yaw){
		// \ N /
		// W O E
		// / S \

		Compass.Direction compDir = Compass.getDirection(yaw);

		String compassLine = "";
		if (line == 0){
			if (compDir == Compass.Direction.NORTH_WEST){
				compassLine = ChatColor.RED + "\\ " + ChatColor.RESET + "N /";
			} else if (compDir == Compass.Direction.NORTH){
				compassLine = "\\ " + ChatColor.RED + "N" + ChatColor.RESET + " /";
			} else if (compDir == Compass.Direction.NORTH_EAST){
				compassLine = "\\ N " + ChatColor.RED + "/" + ChatColor.RESET;
			} else {
				compassLine = "\\ N /";
			}

		} else if (line == 1){
			if (compDir == Compass.Direction.WEST){
				compassLine = ChatColor.RED + "W " + ChatColor.RESET + "O E";
			} else if (compDir == Compass.Direction.EAST){
				compassLine = "W O " + ChatColor.RED + "E" + ChatColor.RESET;
			} else {
				compassLine = "W O E";
			}
		} else if (line == 2){
			if (compDir == Compass.Direction.SOUTH_WEST){
				compassLine = ChatColor.RED + "/ " + ChatColor.RESET + "S \\";
			} else if (compDir == Compass.Direction.SOUTH){
				compassLine = "/ " + ChatColor.RED + "S" + ChatColor.RESET + " \\";
			} else if (compDir == Compass.Direction.SOUTH_EAST){
				compassLine = "/ S " + ChatColor.RED + "\\" + ChatColor.RESET;
			} else {
				compassLine = "/ S \\";
			}
		} else {
			throw new IndexOutOfBoundsException("Index is out of range: " + line);
		}
		return compassLine;
	}

}
