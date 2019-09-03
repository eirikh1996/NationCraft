package io.github.eirikh1996.nationcraft.messages;

import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import io.github.eirikh1996.nationcraft.utils.Compass;
import io.github.eirikh1996.nationcraft.utils.Direction;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Messages {
	private static ArrayList<String> MARKERS = new ArrayList<>();
	public static String ERROR = ChatColor.DARK_RED + "Error: ";
	public static String WARNING = ChatColor.YELLOW + "Warning: ";
	public static String NATIONCRAFT_COMMAND_PREFIX = ChatColor.YELLOW + "[" + ChatColor.AQUA + "Nation" + ChatColor.GRAY + "Craft" + ChatColor.YELLOW + "]"+ ChatColor.RESET;
	public static String CLAIMED_TERRITORY = "%s claimed %d pieces of territory from %s";
	public static String WILDERNESS = ChatColor.DARK_GREEN + "Wilderness";
	public static String MUST_BE_PLAYER = "You must be player to execute this command!";
	public static String NO_PERMISSION = "You don't have permission!";
	static {
		MARKERS.add("/");
		MARKERS.add("\\");
		MARKERS.add("#");
		MARKERS.add("%");
		MARKERS.add("+");
		MARKERS.add("?");
		MARKERS.add("$");
		MARKERS.add("¤");
		MARKERS.add("@");
		MARKERS.add("&");
		MARKERS.add("*");
		MARKERS.add("£");
		MARKERS.add("<");
		MARKERS.add(">");


	}
	public static String[] startUpMessage(){
		String[] message = new String[5];
		message[0] = "||  ||  //\\  ====== ||  //=\\  ";
		message[1] = "||\\|| //==\\   ||   || ||   || ";
		message[2] = "||  || ||  ||   ||   ||  \\=//  ";
		message[3] = "|";
		message[4] = "|";
		return message;
	}
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
			Bukkit.broadcastMessage("Player is facing " + Direction.fromYaw(p.getLocation().getYaw()).name().toLowerCase().replace("_", " "));
		}
		Compass compass = new Compass(Direction.fromYaw(p.getLocation().getYaw()));
		Chunk chunk = p.getLocation().getChunk();
		NationManager nManager = NationManager.getInstance();
		Nation locN = nManager.getNationAt(chunk);
		final int minX = chunk.getX() - 25;
		final int maxX = chunk.getX() + 25;
		final int minZ = chunk.getZ() - 8;
		final int maxZ = chunk.getZ() + 8;
		new BukkitRunnable() {
			@Override
			public void run() {
				@NotNull final Map<Nation, String> nationMarkers = nationMarkers(20, p);
				String header = (locN != null ? nManager.getColor(p,locN) + locN.getName() : ChatColor.DARK_GREEN + "Wilderness");
				int clauseLength = 25 - (ChatColor.stripColor(header).length() / 2);
				int index = 0;
				String leftClause = "";
				String rightClause = "";
				while (index <= clauseLength){
					if (index == 0){
						rightClause += "}";
					} else {
						rightClause += "=";
					}
					if (index == clauseLength){
						leftClause += "{";
					} else {
						leftClause += "=";
					}
					index++;
				}
				p.sendMessage(ChatColor.YELLOW + leftClause + header + ChatColor.YELLOW + rightClause);
				for (int z = minZ ; z <= maxZ ; z++){
					String mapLine = "";
					String compassLine = "";
					int line = z - minZ;
					if (line <= 2) {
						compassLine = compass.getLine(line) + " ";
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
						nations += NationManager.getInstance().getColor(p, listed) + nationMarkers.get(listed) + " " + listed.getName() + " " + ChatColor.RESET;
					}
					p.sendMessage(nations);
				}
				p.sendMessage(ChatColor.YELLOW + "===================================================");
			}
		}.runTaskAsynchronously(NationCraft.getInstance());

	}

	private static Map< Nation, String> nationMarkers( int scanRange, @NotNull Player p){
		Map<Nation, String> returnMap = new HashMap<>();
		Chunk chunk = p.getLocation().getChunk();
		int minX = chunk.getX() - scanRange;
		int maxX = chunk.getX() + scanRange;
		int minZ = chunk.getZ() - scanRange;
		int maxZ = chunk.getZ() + scanRange;
		int index = 0;
		for (int z = minZ ; z <= maxZ ; z++) {
			for (int x = minX; x <= maxX; x++) {
				Territory territory = new Territory(p.getWorld(), x, z);
				Nation foundNation = NationManager.getInstance().getNationAt(territory);
				if (!returnMap.containsKey(foundNation)){
					returnMap.put(foundNation, MARKERS.get(0));
					index++;
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
