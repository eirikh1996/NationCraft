package io.github.eirikh1996.nationcraft.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.Ranks;

public class Messages {
	public static String ERROR = ChatColor.DARK_RED + "Error: ";
	public static String WARNING = ChatColor.YELLOW + "Warning: ";
	public static String MUST_BE_PLAYER = "You must be player to execute this command!";
	public void nationInfo(String nationName, Player p, Boolean ally, Boolean enemy) {
		Nation n = new Nation();
		String name = n.getName();
		String description = n.getDescription();
		List<String> allyList = n.getAllies();
		List<String> enemyList = n.getEnemies();
		Map<Player, Ranks> playerList = n.getPlayers();
		final List<String> onlinePlayerList = new ArrayList();
		final List<String> offlinePlayerList =  new ArrayList();
		while (nationName == n.getName()) {

			for (Player player : playerList.keySet()) {

					if (player.isOnline()) {
						
						String playerName = player.getName();
						Ranks r = playerList.get(player);
						String rankMarker = new String();
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
						String onlinePlayer = rankMarker + playerName + " ";
						onlinePlayerList.add(onlinePlayer);
					} else {
						
						String playerName = player.getName();
						Ranks r = playerList.get(player);
						String rankMarker = new String();
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
						String offlinePlayer = rankMarker + playerName + " ";
						offlinePlayerList.add(offlinePlayer);
					}
					
					
				}
				
			}
			ChatColor color;
			if (ally && !enemy) {
				color = ChatColor.DARK_PURPLE;
			} else if (!ally && enemy) {
				color = ChatColor.RED;
			} else {
				color = ChatColor.WHITE;
			}
			String onlinePlayers = onlinePlayerList.toString();
			String offlinePlayers = offlinePlayerList.toString();
			p.sendMessage(ChatColor.YELLOW + "------------------Nation:" + color + name + ChatColor.YELLOW +  "-----------------");
			p.sendMessage(ChatColor.YELLOW + "Description: " + color + description);
			p.sendMessage(ChatColor.YELLOW + "Allies: " + ChatColor.DARK_PURPLE + allyList.toString());
			p.sendMessage(ChatColor.YELLOW + "Enemies: " + ChatColor.RED + enemyList.toString());
			p.sendMessage(ChatColor.YELLOW + "Players online: " + color + onlinePlayerList.toString());
			p.sendMessage(ChatColor.YELLOW + "Players offline: " + color + offlinePlayerList.toString());
		}

}
