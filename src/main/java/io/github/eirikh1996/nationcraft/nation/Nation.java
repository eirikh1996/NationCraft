package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;



public class Nation {
	private static String name, description, capital;
	private static List<String> allies, enemies, settlements;
	private static List<Chunk> territory;
	private static Map<Player,Ranks> players;
	
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
		settlements = (List<String>) data.get("settlements");
		territory = (List<Chunk>) data.get("territory");
		players = getPlayersAndRanksFromObject(data.get("players"));
	}
	private Map<Player, Ranks> getPlayersAndRanksFromObject(Object obj){
		HashMap<Player, Ranks> returnMap = new HashMap<>();
		HashMap<Object, Object> objMap = (HashMap<Object, Object>) obj;
		for (Object o : objMap.keySet()) {
			Player p = null;
			if (o instanceof UUID){
				p = Bukkit.getPlayer((UUID) o);
			}
			Ranks r = (Ranks) objMap.get(p);
			returnMap.put(p, r);
		}
		return returnMap;
	}
	public void saveNationToFile(){
		String path = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
		File f = new File(path);
		if (!f.exists()){
			f.mkdirs();
		}
		path += "/";
		path += name;
		path += ".nation";
		f = new File(path);
		try {
			PrintWriter writer = new PrintWriter(path);
			writer.println("name: " + name);
			writer.println("description: " + description);
			writer.println("capital: " + capital);
			writer.println("allies:");
			for (int i = 0 ; i <= allies.size() ; i++){
				writer.println("- " + allies.get(i));
			}
			writer.println("enemies:");
			for (int i = 0; i <= enemies.size() ; i++){
				writer.println("- " + enemies.get(i));
			}
			writer.println("settlements:");
			for (int i = 0; i <= settlements.size() ; i++){
				writer.println("- " + settlements.get(i));
			}
			writer.println("territory:");
			for (int i = 0 ; i <= territory.size() ; i++){
				writer.println("- " + territory.get(i));
			}
			writer.println("members:");
			for (Player p : players.keySet()){
				Ranks r = players.get(p);
				writer.println("- " + p.getUniqueId() + ": " + r);
			}
			writer.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}

	}
	public String getName() {
		return name;
	}

	public void setName(String name) { this.name = name; }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description){ this.description = description; }

	public String getCapital(){ return capital; }

	public void setCapital(String capital) { this.capital = capital; }

	public List<String> getAllies(){
		return allies;
	}

	public void setAllies(List<String> allies) { this.allies = allies; }

	public List<String> getEnemies(){
		return enemies;
	}

	public void setEnemies(List<String> enemies) { this.enemies = enemies; }

	public List<String> getSettlements() { return settlements; }
	
	public List<Chunk> getTerritory(){
		return territory;
	}
	
	public Map<Player, Ranks> getPlayers(){
		return players;
	}


	public boolean hasPlayer(Player p) {
		return players.containsKey(p);
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
