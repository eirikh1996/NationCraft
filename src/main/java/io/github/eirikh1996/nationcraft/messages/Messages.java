package io.github.eirikh1996.nationcraft.messages;

import java.util.*;

import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
						Ranks r = playerList.get(player);
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
			p.sendMessage(ChatColor.YELLOW + "Allies: " + ChatColor.DARK_PURPLE + (allyList != null ? allyList.toString() : ""));
			p.sendMessage(ChatColor.YELLOW + "Enemies: " + ChatColor.RED + (enemyList != null ? enemyList.toString() : ""));
			p.sendMessage(ChatColor.YELLOW + "Players online: " + color + onlinePlayers);
			p.sendMessage(ChatColor.YELLOW + "Players offline: " + color + offlinePlayers);
		}

}
