package io.github.eirikh1996.nationcraft.nation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;



public class Nation {
	private static String name, description, capital;
	private static List<String> allies, enemies, settlements;
	private static List<Chunk> territory;
	private static Map<List<Player>,Ranks> players;
	
	public Nation() {
		
	}
	public Nation(File nationFile) {
		Map data = new HashMap<Object, Object>();
		try {
			InputStream input = new FileInputStream(nationFile);
			Yaml yaml = new Yaml();
			data = (Map) yaml.load(input);
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = (String) data.get("name");
		description = (String) data.get("description");
		capital = (String) data.get("capital");
		allies = (List<String>) data.get("allies");
		enemies = (List<String>) data.get("enemies");
		
		
		
		
	}
	private Map<Player, Ranks> getPlayersAndRanksFromObject(Object obj){
		HashMap<Player, Ranks> returnMap = new HashMap<>();
		HashMap<Object, Object> objMap = (HashMap<Object, Object>) obj;
		for (Object o : objMap.keySet()) {
			if (o instanceof String) {
				String str = (String) o;
				String[] parts = str.split(":");
				Player p = null;
				if (parts[0] == p.getUniqueId().toString()) {
					Player returnP = p;
				}
				if ()
			}
		}
		
	}
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public List<String> getAllies(){
		return allies;
	}
	
	public List<String> getEnemies(){
		return enemies;
	}
	
	public List<Chunk> getTerritory(){
		return territory;
	}
	
	public Map<List<Player>, Ranks> getPlayers(){
		return players;
	}
	
	public boolean hasPlayer(Player p) {
		for (List<Player> pList : players.keySet()) {
			if (pList.contains(p)) {
				return true;
			} else {
				return false;
			}
		}
		return false;		
	}
	
	public boolean isAlliedWith(String alliedNation) {
		List<String> alliedNations = allies;
		if (alliedNations.contains(alliedNation)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isAtWarWith(String enemyNation) {
		List<String> enemyNations = enemies;
		if (enemyNations.contains(enemyNation)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOwnNation(Player p) {
		if (players.containsKey(p)) {
			return true;
		} else {
			return false;
		}
		
	}
	public boolean isNationTerritory(Chunk territory) {
		if (this.territory.contains(territory)) {
			return true;
		} else {
			return false;
		}
	}
	
}
