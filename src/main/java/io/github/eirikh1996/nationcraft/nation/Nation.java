package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;



public class Nation {
	private static String name, description, capital;
	private static List<String> allies, enemies, settlements;
	private static List<Chunk> territory;
	private static Map<UUID, Ranks> players;
	private static List<UUID> invitedPlayers;
	private static boolean isOpen;
	
	public Nation(String name, String description, String capital, List<String> allies, List<String> enemies, List<String> settlements, List<Chunk> territory, Map<UUID,Ranks> players) {
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
		territory = chunkListFromObject(data.get("territory"));
		invitedPlayers = playerIDListFromObject(data.get("invitedPlayers"));
		isOpen = (boolean) data.get("isOpen");
		players = getPlayersAndRanksFromObject(data.get("players"));
	}
	private List<UUID> playerIDListFromObject(Object obj){
		List<UUID> returnList = new ArrayList<>();
		List<Object> objList = (List<Object>) obj;
		for (Object o : objList){
			String idStr = (String) o;
			returnList.add(UUID.fromString(idStr));
		}
		return returnList;
	}
	private Map<UUID, Ranks> getPlayersAndRanksFromObject(Object obj){
		HashMap<UUID, Ranks> returnMap = new HashMap<>();
		HashMap<Object, Object> objMap = (HashMap<Object, Object>) obj;
		for (Object o : objMap.keySet()) {
			UUID id = null;
			if (o instanceof UUID){
				id = (UUID) o;
			} else if (o instanceof String){
				String str = (String) o;
				id = UUID.fromString(str);
			}
			Object i = objMap.get(o);
			if (i instanceof Ranks) {
				Ranks r = (Ranks) i;
				returnMap.put(id, r);
			} else if (i instanceof String){
				String str = (String) i;
				Ranks r = Ranks.valueOf(str);
				returnMap.put(id, r);

			}

		}
		return returnMap;
	}

	private List<Chunk> chunkListFromObject(Object obj){
		List<Chunk> returnList = new ArrayList<>();
		List<Object> objList = (List<Object>) obj;
		if (objList == null){
			return returnList;
		}
		for (Object o : objList){
			if (o instanceof ArrayList){
				List<?> objects = (List<?>) o;
				String wName = (String) objects.get(0);
				int x = (Integer) objects.get(1);
				int z = (Integer) objects.get(2);
				World world = Bukkit.getWorld(wName);
				returnList.add(world.getChunkAt(x, z));
			} else if (o instanceof String){

			}
		}
		return returnList;
	}
	public Relation getRelationTo(String nationName){
		return getRelationTo(NationManager.getInstance().getNationByName(nationName));
	}
	public Relation getRelationTo(Nation otherNation){
		if (otherNation == this){
			return null;
		}
		else if (allies.contains(otherNation.getName()) && otherNation.getAllies().contains(name)){
			return Relation.ALLY;
		}
		else if (enemies.contains(otherNation.getName()) || otherNation.getEnemies().contains(name)){
			return Relation.ENEMY;
		} else {
			return Relation.NEUTRAL;
		}
	}
	/**
	 *  Saves the nation data to .nation file
	 *
	 */


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
	
	public Map<UUID, Ranks> getPlayers(){
		return players;
	}

	public boolean addPlayer(Player player){
		if (players.containsKey(player)){
			return false;
		}
		//call event
        NationPlayerJoinEvent event = new NationPlayerJoinEvent(player, this);
		NationCraft.getInstance().getServer().getPluginManager().callEvent(event);
		//check if event was cancelled
		if (event.isCancelled()){
		    return false;
        }
		//if not, add the player and assign recruit rank
		players.put(player.getUniqueId(), Ranks.RECRUIT);
		return true;
	}

	public boolean promotePlayer(Player p){
	    final Ranks origRank = players.get(p.getUniqueId());
	    Ranks newRank = origRank;
	    switch (origRank){
            case RECRUIT:
                newRank = Ranks.MEMBER;
                break;
            case MEMBER:
                newRank = Ranks.OFFICER;
                break;
            case OFFICER:
                newRank = Ranks.OFFICIAL;
                break;
        }
        if (!players.containsKey(p.getUniqueId())){
            return false;
        }
        players.put(p.getUniqueId(), newRank);
        return true;
    }

    public boolean demotePlayer(Player p){
        final Ranks origRank = players.get(p.getUniqueId());
        Ranks newRank = origRank;
        switch (origRank){
            case MEMBER:
                newRank = Ranks.RECRUIT;
                break;
            case OFFICER:
                newRank = Ranks.MEMBER;
                break;
            case OFFICIAL:
                newRank = Ranks.OFFICER;
                break;
        }
        if (!players.containsKey(p.getUniqueId())){
            return false;
        }
        players.put(p.getUniqueId(), newRank);
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

	public List<UUID> getInvitedPlayers() {
		return invitedPlayers;
	}
}
