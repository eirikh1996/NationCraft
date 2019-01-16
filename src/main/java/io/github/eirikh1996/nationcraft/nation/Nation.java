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
	private static List<Player> invitedPlayers;
	private static boolean isOpen;
	
	public Nation(String name, String description, String capital, List<String> allies, List<String> enemies, List<String> settlements, List<Chunk> territory, Map<Player,Ranks> players) {
		this.name = name;
		this.description = description;
		this.capital = capital;
		this.allies = allies;
		this.enemies = enemies;
		this.settlements = settlements;
		this.territory = territory;
		this.players = players;
		invitedPlayers = new ArrayList<>();
	}
	/**
	 * Constructs a nation from the data stored in each nation file
	 * @param nationFile The nation file's path
	 */
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
	/**
	 *  Saves the nation data to .nation file
	 *
	 */
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
				writer.println("- " + territory.get(i).getWorld().getName() + ", " + territory.get(i).getX() + ", " + territory.get(i).getZ() + ")");
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

	/**
	 *
	 * @return The name of a nation
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the nation's name
	 * @param name The nation's name
	 */
	public void setName(String name) { this.name = name; }

	/**
	 * Returns the description of a nation
	 * @return Nation's description
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description){ this.description = description; }

	/**
	 * Get the capital of the nation
	 * @return Nation's capital
	 */
	public String getCapital(){ return capital; }

	/**
	 * Set a settlement as a nation's capital
	 * @param capital
	 */
	public void setCapital(String capital) { this.capital = capital; }

	public List<String> getAllies(){
		return allies;
	}

	public void setAllies(List<String> allies) { this.allies = allies; }

	public boolean addAlly(String ally){
		return allies.add(ally);
	}

	public boolean removeAlly(String ally){
		return allies.remove(ally);
	}

	public List<String> getEnemies(){
		return enemies;
	}

	public boolean addEnemy(String enemy){
		return enemies.add(enemy);
	}

	public boolean removeEnemy(String enemy){
		return enemies.remove(enemy);
	}

	public void setEnemies(List<String> enemies) { this.enemies = enemies; }

	public List<String> getSettlements() { return settlements; }
	
	public List<Chunk> getTerritory(){
		return territory;
	}
	
	public Map<Player, Ranks> getPlayers(){
		return players;
	}

	public boolean addPlayer(Player player){
		if (players.containsKey(player)){
			return false;
		}
		players.put(player, Ranks.RECRUIT);
		return true;
	}

	/**
	 * Returns true if a nation contains given player
	 * @param p Player that is part of a nation
	 * @return true if given player is part of a nation
	 */
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

	public boolean isOpen(){
		return isOpen;
	}

	public List<Player> getInvitedPlayers() {
		return invitedPlayers;
	}
}
