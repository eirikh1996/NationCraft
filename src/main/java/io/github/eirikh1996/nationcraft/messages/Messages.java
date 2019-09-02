package io.github.eirikh1996.nationcraft.messages;

import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import io.github.eirikh1996.nationcraft.utils.Compass;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import org.jetbrains.annotations.NotNull;

public class Messages {
	public static String ERROR = ChatColor.DARK_RED + "Error: ";
	public static String WARNING = ChatColor.YELLOW + "Warning: ";
	public static String NATIONCRAFT_COMMAND_PREFIX = ChatColor.YELLOW + "[" + ChatColor.AQUA + "NationCraft" + ChatColor.YELLOW + "]"+ ChatColor.RESET;
	public static String CLAIMED_TERRITORY = "%s claimed %d pieces of territory from %s";
	public static String WILDERNESS = ChatColor.DARK_GREEN + "Wilderness";
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
			p.sendMessage(ChatColor.YELLOW + "Territory: " + n.getTerritoryManager().size());
			p.sendMessage(ChatColor.YELLOW + "Strength: " + (n.getStrength() >= n.getTerritoryManager().size() ? ChatColor.GREEN : ChatColor.RED) + n.getStrength());
			p.sendMessage(ChatColor.YELLOW + "Maximum strength: " + n.getMaxStrength());
			p.sendMessage(ChatColor.YELLOW + "Allies: " + ChatColor.DARK_PURPLE + (allyList != null ? allyList.toString() : ""));
			p.sendMessage(ChatColor.YELLOW + "Enemies: " + ChatColor.RED + (enemyList != null ? enemyList.toString() : ""));
			p.sendMessage(ChatColor.YELLOW + "Players online: " + color + onlinePlayers);
			p.sendMessage(ChatColor.YELLOW + "Players offline: " + color + offlinePlayers);
		}
	public static void generateTerritoryMap(Player p){
		if (Settings.Debug){
			Bukkit.broadcastMessage("Player is facing " + p.getFacing().name().toLowerCase().replace("_", " "));
		}
		Compass compass = new Compass(p.getFacing());
		Chunk chunk = p.getLocation().getChunk();
		NationManager nManager = NationManager.getInstance();
		Nation locN = nManager.getNationAt(chunk);
		int minX = chunk.getX() - 20;
		int maxX = chunk.getX() + 20;
		int minZ = chunk.getZ() - 10;
		int maxZ = chunk.getZ() + 10;
		@NotNull final Map<Nation, String> nationMarkers = nationMarkers(20, p);
		p.sendMessage(ChatColor.YELLOW + "============={" + (locN != null ? nManager.getColor(p,locN) + locN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.YELLOW + "}===========");
		for (int z = minZ ; z <= maxZ ; z++){
			String mapLine = "";
			String compassLine = "";
			int line = z - minZ;
			if (line <= 2) {
				compassLine = compass.getLine(line);
			}
			for (int x = minX ; x <= maxX ; x++){
				int column = x - minX;
				Territory territory = new Territory(p.getWorld(), x, z);
				if (line <= 2 && column <= 4){
					continue;
				}
				mapLine += getTerritoryMarker(nationMarkers, territory, p);

			}
			p.sendMessage(compassLine + mapLine);
		}
		if (!nationMarkers.isEmpty()){
			String nations = "";
			for (Nation listed : nationMarkers.keySet()){
				if (listed == null){
					continue;
				}
				nations += NationManager.getInstance().getColor(p, listed) + listed.getName() + " " + ChatColor.RESET;
			}
			p.sendMessage(nations);
		}
		p.sendMessage(ChatColor.YELLOW + "==============================================");
	}

	private static Map< Nation, String> nationMarkers( int scanRange, @NotNull Player p){
		String[] markerArray = new String[]{"/", "\\", "#", "%","+","?","$","¤","@","&","*","£","<", ">"};
		LinkedList<String> markers = new LinkedList<>();
		markers.addAll(Arrays.asList(markerArray));
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
					returnMap.put(foundNation, markers.pop());
				}
			}
		}
		return returnMap;
	}
	private static String getTerritoryMarker(Map<Nation, String> nationMarkers , Territory territory, Player player){
        Chunk pChunk = player.getLocation().getChunk();
		String marker = "-";
		Nation n = NationManager.getInstance().getNationAt(territory);
		if (n != null ){
			marker = NationManager.getInstance().getColor(player,n) + nationMarkers.get(n) + ChatColor.RESET;
		}
		if (territory.equals(Territory.fromChunk(pChunk))){
			marker = ChatColor.BLUE + "+" + ChatColor.RESET;
		}
		return marker;
	}

}
