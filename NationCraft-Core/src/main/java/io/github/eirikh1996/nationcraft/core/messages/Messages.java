package io.github.eirikh1996.nationcraft.core.messages;


import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.nation.Relation;
import io.github.eirikh1996.nationcraft.api.objects.NCVector;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatTextComponent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import io.github.eirikh1996.nationcraft.api.utils.Compass;
import io.github.eirikh1996.nationcraft.api.utils.Direction;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.Ranks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class Messages {
	private static ArrayList<String> MARKERS = new ArrayList<>();
	public static NationCraftMain main;
	public static String ERROR = TextColor.DARK_RED + "Error: ";
	public static String WARNING = TextColor.YELLOW + "Warning: ";
	public static TextComponent NATIONCRAFT_COMMAND_PREFIX = Component
			.text("[", NamedTextColor.YELLOW)
			.append(Component.text("Nation", NamedTextColor.AQUA))
			.append(Component.text("Craft", NamedTextColor.GRAY))
			.append(Component.text("] ", NamedTextColor.YELLOW));
	public static String NOT_IN_A_NATION = "You are not in a nation";
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
		MARKERS.add(14,"{");
		MARKERS.add(15, "}");
		MARKERS.add(16, "[");
		MARKERS.add(17, "]");
		MARKERS.add(18, "A");



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
	public static void nationInfo(Nation n, NCPlayer p) {
		String name = n.getName();
		NamedTextColor color = n.getColor(p);
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
				if (!ally.isAlliedWith(n) || ally == n){
					continue;
				}
				allyList.add(ally.getName());
			}
		}
		List<String> enemyList = new ArrayList<>();
		if (!n.getEnemies().isEmpty()){
			for (Nation enemy : n.getEnemies()){
				enemyList.add(enemy.getName());
			}
		}
		for (Nation enemy : NationManager.getInstance()){
			if (enemyList.contains(enemy.getName()) || !enemy.isAtWarWith(n) || enemy == n){
				continue;
			}
			enemyList.add(enemy.getName());
		}
		List<String> truceList = new ArrayList<>();
		for (Nation truce : NationManager.getInstance()) {
			if (truce == n || !truce.isTrucedWith(n)) {
				continue;
			}
			truceList.add(n.getName());
		}

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
			final TextComponent nationInfo = Component.text("------------------{ Nation: ", NamedTextColor.YELLOW).append(Component.text(name, color)).append(Component.text(" }-----------------", NamedTextColor.YELLOW));
			nationInfo.appendNewline().append(Component.text("Description: ", NamedTextColor.YELLOW).append(Component.text(description, color)));
			nationInfo.appendNewline().append(Component.text("Capital: " + capital, NamedTextColor.YELLOW));
			nationInfo.appendNewline().append(Component.text("Settlements: ", NamedTextColor.YELLOW).append(Component.text(String.join(", ", settlementNames), NamedTextColor.GREEN)));
			nationInfo.appendNewline().append(Component.text("Territory: ").append(Component.text(n.getTerritory().size(), (n.isStrongEnough() ? NamedTextColor.GREEN : NamedTextColor.RED))));
			nationInfo.appendNewline().append(Component.text("Power: " ).append(Component.text(n.getName(), (n.getPower() >= n.getTerritory().size() ? (n.getPower() == n.getTerritory().size() ? NamedTextColor.YELLOW :NamedTextColor.GREEN) : NamedTextColor.RED))));
			nationInfo.appendNewline().append(Component.text("Maximum strength: " + n.getMaxPower(), NamedTextColor.YELLOW));
			nationInfo.appendNewline().append(Component.text("Allies: ", NamedTextColor.YELLOW).append(Component.text(allyList.isEmpty() ? "None" : String.join(", ", allyList), NationSettings.RelationColors.getOrDefault(Relation.ALLY, NamedTextColor.DARK_PURPLE))));
			nationInfo.appendNewline().append(Component.text("Truces: ", NamedTextColor.YELLOW).append(Component.text(truceList.isEmpty() ? "None" : String.join(", ", allyList), NationSettings.RelationColors.getOrDefault(Relation.TRUCE, NamedTextColor.LIGHT_PURPLE))));
			nationInfo.appendNewline().append(Component.text("Enemies: ", NamedTextColor.YELLOW).append(Component.text(enemyList.isEmpty() ? "None" : String.join(", ", allyList), NationSettings.RelationColors.getOrDefault(Relation.ENEMY, NamedTextColor.RED))));
			nationInfo.appendNewline().append(Component.text("Players online: ", NamedTextColor.YELLOW).append(Component.text(String.join(", ", onlinePlayers), color)));
			nationInfo.appendNewline().append(Component.text("Players offline: ", NamedTextColor.YELLOW).append(Component.text(String.join(", ", offlinePlayers), color)));
			p.sendMessage(nationInfo);
	}
	public static void generateTerritoryMap(NCPlayer p){
		Compass compass = new Compass(Direction.fromYaw(p.getLocation().getYaw()));
		int cx = p.getLocation().getBlockX() >> 4;
		int cz = p.getLocation().getBlockZ() >> 4;
		NationManager nManager = NationManager.getInstance();
		Nation locN = p.getLocation().getNation();
		final int minX = cx - 25;
		final int maxX = cx + 25;
		final int minZ = cz - p.getMapHeight() / 2;
		final int maxZ = cz + p.getMapHeight() / 2;
		@NotNull final Map<Nation, String> nationMarkers = nationMarkers(p);
		TextComponent header = Component.text(cx + ", " + cz)
				.append(Component.text("}==={", NamedTextColor.YELLOW))
				.append((locN != null ? locN.getName(p) : Component.text("Wilderness", NamedTextColor.DARK_GREEN)));
		int headerLength = PlainTextComponentSerializer.plainText().serialize(header).length();
		int clauseLength = 25 - (headerLength / 2);
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
		TextComponent territoryMap = Component.text(leftClause, NamedTextColor.YELLOW)
						.append(header)
						.append(Component.text(rightClause, NamedTextColor.YELLOW))
						.appendNewline();

		//North-South
		final int totalLines = maxZ - minZ;
		for (int z = minZ ; z <= maxZ ; z++){
			ChatText.Builder mapLine = ChatText.builder();
			ChatText.Builder compassLine = ChatText.builder();
			int line = z - minZ;
			if (line <= 2) {
				territoryMap = territoryMap.append(compass.getLine(line)).appendSpace();
			}
			//West-East
			for (int x = minX ; x <= maxX ; x++){
				int column = x - minX;
				Territory territory = new Territory(p.getLocation().getWorld(), x, z);
				if (line <= 2 && column <= 4){
					continue;
				}
				territoryMap = territoryMap.append(getTerritoryMarker(nationMarkers, territory, p));

			}
			territoryMap = territoryMap.appendNewline();
		}
		if (!nationMarkers.isEmpty()){
			for (Nation listed : nationMarkers.keySet()){
				if (listed == null){
					continue;
				}
				territoryMap = territoryMap.append(
						Component.text(nationMarkers.get(listed) + " " + listed.getName(), listed.getColor(p))
				);
			}
		}
		territoryMap = territoryMap.appendNewline();
		territoryMap = territoryMap.append(Component.text("===================================================", NamedTextColor.YELLOW));
		p.sendMessage(territoryMap);
	}

	public static void nearestSettlements(NCPlayer player){
		HashMap<Settlement, Integer> nearestSettlement = new HashMap<>();
		for (Settlement s : SettlementManager.getInstance()){
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
		int cx = p.getLocation().getChunkX();
		int cz = p.getLocation().getChunkZ();
		final int minX = cx - 25;
		final int maxX = cx + 25;
		final int minZ = cz - p.getMapHeight() / 2;
		final int maxZ = cz + p.getMapHeight() / 2;
		int index = 0;
		for (int z = minZ ; z <= maxZ ; z++) {
			for (int x = minX; x <= maxX; x++) {
				Territory territory = new Territory(p.getWorld(), x, z);
				Nation foundNation = territory.getNation();
				if (!returnMap.containsKey(foundNation)){
					returnMap.put(foundNation, MARKERS.get(index));
					index++;
				}
			}
		}
		return returnMap;
	}
	private static TextComponent getTerritoryMarker(Map<Nation, String> nationMarkers , Territory territory, NCPlayer player){
		TextComponent marker = Component.text("-");
		@Nullable final Nation n = territory.getNation();
		Settlement settlement = territory.getSettlement();

		if (n != null ){
			marker = Component.text(nationMarkers.get(n), n.getColor(player))
					.hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(n.getName(player)));
		}
		if (settlement != null){
			if (settlement.getTownCenter().equalsTerritory(territory)){
				marker = Component.text("T", NamedTextColor.DARK_BLUE).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
						Component.text(settlement.getName() + "'s town center", NamedTextColor.DARK_BLUE)
				));
			} else if (n != null && n.getCapital() != null && n.getCapital().equals(settlement)){
				marker = Component.text("C", NamedTextColor.DARK_BLUE).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
						Component.text(settlement.getName() + ", capital of nation " + settlement.getNation().getName(), NamedTextColor.DARK_BLUE)
				));
            }
			else {
				marker = Component.text("S", NamedTextColor.DARK_BLUE).hoverEvent(net.kyori.adventure.text.event.HoverEvent.showText(
						Component.text(settlement.getName() + ", settlement of " + settlement.getNation().getName(), NamedTextColor.DARK_BLUE)
				));
			}

		}
		if (territory.contains(player.getLocation())){
			marker = Component.text("+", NamedTextColor.BLUE).hoverEvent(
					HoverEvent.showText(Component.text("Your position", NamedTextColor.GRAY))
			);
		}
		return marker;
	}

	public static void displayPlayerInfo(NCCommandSender sender){
		if (!(sender instanceof NCPlayer))
			return;
		displayPlayerInfo(sender, (NCPlayer) sender);
	}

	public static void displayPlayerInfo(NCCommandSender sender, NCPlayer target){
		final String[] powerBar = new String[103];
		double ratio = Settings.player.MaxPower / 101.0;

		powerBar[0] = TextColor.GOLD + "[" + TextColor.RESET;
		powerBar[102] = TextColor.GOLD + "]" + TextColor.RESET;
		float percent = (float) ((target.getPower() / Settings.player.MaxPower ) * 100f);
		for (int i = 1 ; i < 102 ; i++){
			double powerLevel = ratio * i;
			String point = "";

			if (powerLevel <= target.getPower()){
				point += getTextColor(percent);
			} else {
				point += TextColor.GRAY;
			}
			point += "|" + TextColor.RESET;
			powerBar[i] = point;
		}
		final Date lastLogin = new Date(target.getLastActivityTime());


		sender.sendMessage("§8========={ §3Player " + target.getName() + "§8}=========");
		sender.sendMessage(String.format("§7Power: §3%.2f / %.2f", target.getPower(), Settings.player.MaxPower));
		sender.sendMessage(String.join("", powerBar));
		sender.sendMessage("§7Last activity: " + (target.isOnline() ? TextColor.GREEN + "Currently online " : TextColor.DARK_AQUA + lastLogin.toString()));
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
