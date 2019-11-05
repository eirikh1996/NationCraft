package io.github.eirikh1996.nationcraft.messages;


import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import io.github.eirikh1996.nationcraft.utils.Compass;
import io.github.eirikh1996.nationcraft.utils.Direction;
import io.github.eirikh1996.nationcraft.utils.TopicPaginator;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Messages {
	private static ArrayList<String> MARKERS = new ArrayList<>();
	public static String ERROR = ChatColor.DARK_RED + "Error: ";
	public static String WARNING = ChatColor.YELLOW + "Warning: ";
	public static String NATIONCRAFT_COMMAND_PREFIX = ChatColor.YELLOW + "[" + ChatColor.AQUA + "Nation" + ChatColor.GRAY + "Craft" + ChatColor.YELLOW + "] "+ ChatColor.RESET;
	public static String CLAIMED_TERRITORY = "%s claimed %d pieces of territory from %s";
	public static String WILDERNESS = ChatColor.DARK_GREEN + "Wilderness";
	public static String MUST_BE_PLAYER = "You must be player to execute this command!";
	public static String NO_PERMISSION = "You don't have permission!";
	static {
		MARKERS.add(0,"/" );
		MARKERS.add(1,"\\");
		MARKERS.add(2,"#");
		MARKERS.add(3,"%");
		MARKERS.add(4,"+");
		MARKERS.add(5,"?");
		MARKERS.add(6,"$");
		MARKERS.add(7,"¤");
		MARKERS.add(8,"@");
		MARKERS.add(9,"&");
		MARKERS.add(10,"*");
		MARKERS.add(11,"£");
		MARKERS.add(12,"<");
		MARKERS.add(13,">");


	}
	public static String[] startUpMessage(){
		String[] message = new String[5];
		message[0] = ChatColor.AQUA.toString() + "||  ||  //\\  ====== ||  //=\\  ||";
		message[1] = "||\\|| //==\\   ||   || ||   || ||";
		message[2] = "||  || ||  ||   ||   ||  \\=//  ||";
		return message;
	}
	public static void nationInfo(Nation n, Player p, ChatColor color) {
		String name = n.getName();
		String description = n.getDescription();
		String capital = n.getCapital() != null ? n.getCapital().getName(): "None";
		ArrayList<String> settlementNames = new ArrayList<>();
		for (Settlement settlement : n.getSettlements()){

			if (settlement == null || n.getCapital().equals(settlement)){
				continue;
			}
			settlementNames.add(settlement.getName());

		}
		List<String> allyList = new ArrayList<>();
		if (!n.getAllies().isEmpty()){
			for (Nation ally : n.getAllies()){
				if (ally.isAlliedWith(n)){
					continue;
				}
				allyList.add(ally.getName());
			}
		}
		List<String> enemyList = new ArrayList<>();
		if (!n.getEnemies().isEmpty()){
			for (Nation enemy : n.getEnemies()){
				enemyList.add(n.getName());
			}
		}
		for (Nation enemy : NationManager.getInstance()){
			if (enemyList.contains(enemy.getName()) || !enemy.isAtWarWith(n)){
				continue;
			}
			enemyList.add(enemy.getName());
		}
		List<String> truceList = new ArrayList<>();

		Map<NCPlayer, Ranks> playerList = n.getPlayers();
		String onlinePlayers = "";
		String offlinePlayers = "";

			for (NCPlayer np : playerList.keySet()) {
				if (Bukkit.getPlayer(np.getPlayerID()) != null) {
					Player player = Bukkit.getPlayer(np.getPlayerID());
					String playerName = player.getName();
					Ranks r = playerList.get(np);
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
					OfflinePlayer player = Bukkit.getOfflinePlayer(np.getPlayerID());
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
			Collections.sort(allyList);
			Collections.sort(truceList);
			Collections.sort(enemyList);
			p.sendMessage(ChatColor.YELLOW + "------------------{ Nation:" + color + " " + name + ChatColor.YELLOW +  " }-----------------");
			p.sendMessage(ChatColor.YELLOW + "Description: " + color + description);
			p.sendMessage(ChatColor.YELLOW + "Capital: " + capital);
			p.sendMessage(ChatColor.YELLOW + "Settlements: " + ChatColor.GREEN + String.join(", ", settlementNames));
			p.sendMessage(ChatColor.YELLOW + "Territory: " + (n.isStrongEnough() ? ChatColor.GREEN : ChatColor.RED) + n.getTerritoryManager().size());
			p.sendMessage(ChatColor.YELLOW + "Strength: " + (n.getStrength() >= n.getTerritoryManager().size() ? ChatColor.GREEN : ChatColor.RED) + n.getStrength());
			p.sendMessage(ChatColor.YELLOW + "Maximum strength: " + n.getMaxStrength());
			p.sendMessage(ChatColor.YELLOW + "Allies: " + ChatColor.DARK_PURPLE + allyList.toString());
			p.sendMessage(ChatColor.YELLOW + "Truces: " + ChatColor.LIGHT_PURPLE + truceList.toString());
			p.sendMessage(ChatColor.YELLOW + "Enemies: " + ChatColor.RED + enemyList.toString());
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

	public static void nearestSettlements(Player player){
		HashMap<Settlement, Integer> nearestSettlement = new HashMap<>();
		for (Settlement s : SettlementManager.getInstance().getAllSettlements()){
			Vector pLoc = player.getLocation().toVector();
			pLoc.setY(0);
			Vector sLoc = s.getTownCenter().getCenterPoint();
			pLoc.setY(0);
			int distance = (int) pLoc.subtract(sLoc).length();
			nearestSettlement.put(s, distance);
		}

		//Sort by distance in ascending order
		Map<Settlement, Integer> sorted = nearestSettlement
				.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		player.sendMessage(ChatColor.YELLOW + "================{" + ChatColor.RESET + "Nearest settlements" + ChatColor.YELLOW + "}================");
		int count = 0;
		for (Settlement s : sorted.keySet()){
			player.sendMessage(s.getName() + String.format(" [%d, %d]", s.getTownCenter().getCenterPoint().getBlockX(), s.getTownCenter().getCenterPoint().getBlockZ()) + String.format(" %d blocks away", sorted.get(s)));
			if (count >= 9){
				break;
			}
			count++;
		}
		player.sendMessage(ChatColor.YELLOW + "=====================================================");
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
					returnMap.put(foundNation, MARKERS.get(index));
					index++;
				}
			}
		}
		return returnMap;
	}
	private static String getTerritoryMarker(Map<Nation, String> nationMarkers , Territory territory, Player player){
        Chunk pChunk = player.getLocation().getChunk();
		String marker = "-";
		@Nullable final Nation n = NationManager.getInstance().getNationAt(territory);
		Settlement settlement = null;

		if (n != null ){
			marker = NationManager.getInstance().getColor(player,n) + nationMarkers.get(n) + ChatColor.RESET;
			for (Settlement s : n.getSettlements()){
				if (s == null || !s.getTerritory().contains(territory)){
					continue;
				}
				settlement = s;
			}
			if (n.getCapital() != null && (n.getCapital().getTerritory().contains(territory) || n.getCapital().getTownCenter().equalsTerritory(territory))){
				settlement = n.getCapital();
			}
		}
		if (settlement != null){
			if (settlement.getTownCenter().equalsTerritory(territory)){
				marker = ChatColor.DARK_BLUE + "T" + ChatColor.RESET;
			} else if (n.getCapital().equals(settlement)){
                marker = ChatColor.GRAY + "C" + ChatColor.RESET;
            }
			else {
				marker = ChatColor.GRAY + "S" + ChatColor.RESET;
			}

		}
		if (territory.equals(Territory.fromChunk(pChunk))){
			marker = ChatColor.BLUE + "+" + ChatColor.RESET;
		}
		return marker;
	}

}
