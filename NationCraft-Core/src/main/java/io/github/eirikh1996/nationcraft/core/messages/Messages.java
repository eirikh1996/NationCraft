package io.github.eirikh1996.nationcraft.core.messages;


import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCVector;
import io.github.eirikh1996.nationcraft.api.objects.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.territory.Territory;
import io.github.eirikh1996.nationcraft.core.utils.Compass;
import io.github.eirikh1996.nationcraft.core.utils.Direction;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.Ranks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class Messages {
	private static ArrayList<String> MARKERS = new ArrayList<>();
	public static NationCraftMain main;
	public static String ERROR = TextColor.DARK_RED + "Error: ";
	public static String WARNING = TextColor.YELLOW + "Warning: ";
	public static String NATIONCRAFT_COMMAND_PREFIX = TextColor.YELLOW + "[" + TextColor.AQUA + "Nation" + TextColor.GRAY + "Craft" + TextColor.YELLOW + "] "+ TextColor.RESET;
	public static String CLAIMED_TERRITORY = "%s claimed %d pieces of territory from %s";
	public static String WILDERNESS = TextColor.DARK_GREEN + "Wilderness";
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
	public static void initialize(NationCraftMain main) {
		Messages.main = main;
	}

	public static void logMessage() {
		final NCConsole log = main.getConsole();
		log.sendMessage("");
		log.sendMessage(TextColor.AQUA + "|\\  |  /\\ === | /--\\ |\\  |" + TextColor.GRAY + " /--  |--\\  /\\  |== ===");
		log.sendMessage(TextColor.AQUA + "| \\ | /__\\ |  ||   | | \\ ||  " + TextColor.GRAY + "  |--/  /__\\ |=   | ");
		log.sendMessage(TextColor.AQUA + "|  \\|/    \\|  | \\--/ |  \\| " + TextColor.GRAY + "\\-- |  \\/    \\|    | ");
		log.sendMessage("");
	}
	public static void nationInfo(Nation n, NCPlayer p, TextColor color) {
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
		Set<String> onlinePlayers = new HashSet<>();
		Set<String> offlinePlayers = new HashSet<>();

			for (NCPlayer np : playerList.keySet()) {
				if (np.isOnline()) {
					onlinePlayers.add(rankMarker(np, playerList.get(np)) + np.getName());
				} else {
					offlinePlayers.add(rankMarker(np, playerList.get(np)) + np.getName());
				}

			}
			Collections.sort(allyList);
			Collections.sort(truceList);
			Collections.sort(enemyList);
			p.sendMessage(TextColor.YELLOW + "------------------{ Nation:" + color + " " + name + TextColor.YELLOW +  " }-----------------");
			p.sendMessage(TextColor.YELLOW + "Description: " + color + description);
			p.sendMessage(TextColor.YELLOW + "Capital: " + capital);
			p.sendMessage(TextColor.YELLOW + "Settlements: " + TextColor.GREEN + String.join(", ", settlementNames));
			p.sendMessage(TextColor.YELLOW + "Territory: " + (n.isStrongEnough() ? TextColor.GREEN : TextColor.RED) + n.getTerritoryManager().size());
			p.sendMessage(TextColor.YELLOW + "Power: " + (n.getPower() >= n.getTerritoryManager().size() ? (n.getPower() == n.getTerritoryManager().size() ? TextColor.YELLOW :TextColor.GREEN) : TextColor.RED) + n.getPower());
			p.sendMessage(TextColor.YELLOW + "Maximum strength: " + n.getMaxPower());
			p.sendMessage(TextColor.YELLOW + "Allies: " + TextColor.DARK_PURPLE + allyList.toString());
			p.sendMessage(TextColor.YELLOW + "Truces: " + TextColor.LIGHT_PURPLE + truceList.toString());
			p.sendMessage(TextColor.YELLOW + "Enemies: " + TextColor.RED + enemyList.toString());
			p.sendMessage(TextColor.YELLOW + "Players online: " + color + String.join(", ", onlinePlayers));
			p.sendMessage(TextColor.YELLOW + "Players offline: " + color + String.join(", ", offlinePlayers));
		}
	public static void generateTerritoryMap(NCPlayer p){
		Compass compass = new Compass(Direction.fromYaw(p.getLocation().getYaw()));
		int cx = p.getLocation().getBlockX() >> 4;
		int cz = p.getLocation().getBlockZ() >> 4;
		NationManager nManager = NationManager.getInstance();
		Nation locN = nManager.getNationAt(p.getLocation());
		final int minX = cx - 25;
		final int maxX = cx + 25;
		final int minZ = cz - p.getMapHeight() / 2;
		final int maxZ = cz + p.getMapHeight() / 2;
		@NotNull final Map<Nation, String> nationMarkers = nationMarkers(p);
		String header = (locN != null ? nManager.getColor(p, locN) + locN.getName() : TextColor.DARK_GREEN + "Wilderness");
		int clauseLength = 25 - (TextColor.strip(header).length() / 2);
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
		p.sendMessage(TextColor.YELLOW + leftClause + header + TextColor.YELLOW + rightClause);
		for (int z = minZ ; z <= maxZ ; z++){
			String mapLine = "";
			String compassLine = "";
			int line = z - minZ;
			if (line <= 2) {
				compassLine = compass.getLine(line) + " ";
			}
			for (int x = minX ; x <= maxX ; x++){
				int column = x - minX;
				Territory territory = new Territory(p.getLocation().getWorld(), x, z);
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
				nations += NationManager.getInstance().getColor(p, listed) + nationMarkers.get(listed) + " " + listed.getName() + " " + TextColor.RESET;
			}
			p.sendMessage(nations);
		}
		p.sendMessage(TextColor.YELLOW + "===================================================");
	}

	public static void nearestSettlements(NCPlayer player){
		HashMap<Settlement, Integer> nearestSettlement = new HashMap<>();
		for (Settlement s : SettlementManager.getInstance().getAllSettlements()){
			NCVector pLoc = player.getLocation().toVector();
			pLoc.setY(0);
			NCVector sLoc = s.getTownCenter().getCenterPoint();
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

		if (sorted.isEmpty()) {
		    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "No settlements nearby");
		    return;
        }
		player.sendMessage(TextColor.YELLOW + "================={" + TextColor.RESET + "Nearest settlements" + TextColor.YELLOW + "}=================");
		int count = 0;
		for (Settlement s : sorted.keySet()){
			player.sendMessage(s.getName() + String.format(" [%d, %d]", s.getTownCenter().getCenterPoint().getBlockX(), s.getTownCenter().getCenterPoint().getBlockZ()) + String.format(" %d blocks away", sorted.get(s)));
			if (count >= 9){
				break;
			}
			count++;
		}
		player.sendMessage(TextColor.YELLOW + "=====================================================");
	}

	private static String rankMarker(NCPlayer player, Ranks r) {
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
		return rankMarker;
	}

	private static Map< Nation, String> nationMarkers( @NotNull NCPlayer p){
		Map<Nation, String> returnMap = new HashMap<>();
		int cx = p.getLocation().getBlockX() >> 4;
		int cz = p.getLocation().getBlockZ() >> 4;
		final int minX = cx - 25;
		final int maxX = cx + 25;
		final int minZ = cz - p.getMapHeight() / 2;
		final int maxZ = cz + p.getMapHeight() / 2;
		int index = 0;
		for (int z = minZ ; z <= maxZ ; z++) {
			for (int x = minX; x <= maxX; x++) {
				Territory territory = new Territory(p.getLocation().getWorld(), x, z);
				Nation foundNation = NationManager.getInstance().getNationAt(territory);
				if (!returnMap.containsKey(foundNation)){
					returnMap.put(foundNation, MARKERS.get(index));
					index++;
				}
			}
		}
		return returnMap;
	}
	private static String getTerritoryMarker(Map<Nation, String> nationMarkers , Territory territory, NCPlayer player){
		String marker = "-";
		@Nullable final Nation n = NationManager.getInstance().getNationAt(territory);
		Settlement settlement = null;

		if (n != null ){
			marker = NationManager.getInstance().getColor(player,n) + nationMarkers.get(n) + TextColor.RESET;
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
				marker = TextColor.DARK_BLUE + "T" + TextColor.RESET;
			} else if (n.getCapital().equals(settlement)){
                marker = TextColor.GRAY + "C" + TextColor.RESET;
            }
			else {
				marker = TextColor.GRAY + "S" + TextColor.RESET;
			}

		}
		if (territory.contains(player.getLocation())){
			marker = TextColor.BLUE + "+" + TextColor.RESET;
		}
		return marker;
	}

	public static void displayPlayerInfo(NCPlayer player){
		final String[] powerBar = new String[103];
		double ratio = Settings.PlayerMaxPower / 101.0;

		powerBar[0] = TextColor.GOLD + "[" + TextColor.RESET;
		powerBar[102] = TextColor.GOLD + "]" + TextColor.RESET;
		float percent = (float) ((player.getPower() / Settings.PlayerMaxPower ) * 100f);
		for (int i = 1 ; i < 102 ; i++){
			double powerLevel = ratio * i;
			String point = "";

			if (powerLevel <= player.getPower()){
				point += getTextColor(percent);
			} else {
				point += TextColor.GRAY;
			}
			point += "|" + TextColor.RESET;
			powerBar[i] = point;
		}
		final Date lastLogin = new Date(player.getLastActivityTime());


		player.sendMessage("========={ Player " + player.getName() + "}=========");
		player.sendMessage(String.format("Power: %.2f / %.2f", player.getPower(), Settings.PlayerMaxPower));
		player.sendMessage(String.join("", powerBar));
		player.sendMessage("Last activity: " + (player.isOnline() ? TextColor.GREEN + "Currently online " : lastLogin.toString()));
	}

	private static TextColor getTextColor(float percent){
		if (percent <= 10f){
			return TextColor.DARK_RED;
		} else if (percent <=30f) {
		    return TextColor.RED;
        } else if (percent <= 70f) {
			return TextColor.YELLOW;
		} else if (percent <= 85f) {
		    return TextColor.GREEN;
        }

		else {
			return TextColor.DARK_GREEN;
		}
	}

}
