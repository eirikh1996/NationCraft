package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerInviteEvent;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerJoinEvent;
import io.github.eirikh1996.nationcraft.exception.NationNotFoundException;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import io.github.eirikh1996.nationcraft.territory.NationTerritoryManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import io.github.eirikh1996.nationcraft.territory.TerritoryManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;



final public class Nation implements Comparable<Nation>, Cloneable {
	@NotNull private final UUID uuid;
	@NotNull private final String originalName;
	@NotNull private String name, description, capital;
	@NotNull private final List<String> allies, enemies, settlements;
	@NotNull private final TerritoryManager territoryManager;
	@NotNull private final Map<UUID, Ranks> players;
	@NotNull private final Set<UUID> invitedPlayers;
	private final boolean isOpen;
	
	public Nation(@NotNull String name, @NotNull String description, @NotNull String capital, @NotNull List<String> allies, @NotNull List<String> enemies, @NotNull List<String> settlements, @NotNull Map<UUID,Ranks> players) {
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = description;
		this.capital = capital;
		this.allies = allies;
		this.enemies = enemies;
		this.settlements = settlements;
		this.players = players;
		isOpen = false;
		invitedPlayers = new HashSet<>();
		territoryManager = new NationTerritoryManager(this);
	}

	public Nation(@NotNull String name, Player leader){
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = "Default description";
		this.capital = "(none)";
		this.allies = new ArrayList<>();
		this.enemies = new ArrayList<>();
		this.settlements = new ArrayList<>();
		this.players = new HashMap<>();
		players.put(leader.getUniqueId(), Ranks.LEADER);
		isOpen = false;
		invitedPlayers = new HashSet<>();
		territoryManager = new NationTerritoryManager(this);
	}
	/**
	 * Constructs a nation from the data stored in each nation file
	 * @param nationFile The nation file's path
	 */
	public Nation(File nationFile) {
		final Map data;
		try {
			InputStream input = new FileInputStream(nationFile);
			Yaml yaml = new Yaml();
			data = (Map) yaml.load(input);
			input.close();
		} catch (IOException e) {
			throw new NationNotFoundException("File at " + nationFile.getAbsolutePath() + " was fot found!");
		}
		uuid = UUID.fromString((String) data.get("uuid"));
		name = (String) data.get("name");
		originalName = (String) data.get("originalName");
		description = (String) data.get("description");
		capital = (String) data.get("capital");
		allies = stringListFromObject("allies");
		enemies = stringListFromObject("enemies");
		settlements = stringListFromObject("settlements");
		invitedPlayers = playerIDListFromObject(data.get("invitedPlayers"));
		isOpen = (boolean) data.get("isOpen");
		players = getPlayersAndRanksFromObject(data.get("players"));
		territoryManager = new NationTerritoryManager(this);
		territoryManager.addAll(chunkListFromObject(data.get("territory")));
	}
	private List<String> stringListFromObject(Object obj){
		ArrayList<String> returnList = new ArrayList<>();
		if (obj == null){
			return Collections.emptyList();
		} else if (obj instanceof ArrayList) {
			List<Object> objList = (List<Object>) obj;
			for (Object o : objList) {
				if (o instanceof String) {
					String str = (String) o;
					returnList.add(str);
				}
			}
		} else if (obj instanceof String){
			String string = (String) obj;
			if (string == null || string.length() == 0){
				return Collections.emptyList();
			}
			returnList.add(string);
		}
		return returnList;
	}
	private Set<UUID> playerIDListFromObject(Object obj){
		Set<UUID> returnList = new HashSet<>();
		List<Object> objList = (List<Object>) obj;
		if (objList == null){
			returnList = Collections.emptySet();
		} else {
			for (Object o : objList) {
				String idStr = (String) o;
				returnList.add(UUID.fromString(idStr));
			}
		}
		return returnList;
	}
	private Map<UUID, Ranks> getPlayersAndRanksFromObject(Object obj){
		HashMap<UUID, Ranks> returnMap = new HashMap<>();
		HashMap<Object, Object> objMap = (HashMap<Object, Object>) obj;
		if (objMap == null){
			return Collections.emptyMap();
		}
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

	private Set<Territory> chunkListFromObject(Object obj){
		HashSet<Territory> returnList = new HashSet<>();
		ArrayList objList = (ArrayList) obj;
		if (objList == null){
			return returnList;
		}
		for (Object o : objList){
			if (o instanceof ArrayList){
				List<?> objects = (List<?>) o;
				UUID wID = UUID.fromString((String) objects.get(0));
				int x = (Integer) objects.get(1);
				int z = (Integer) objects.get(2);
				World world = NationCraft.getInstance().getServer().getWorld(wID);
				returnList.add(new Territory(world,x,z));
			} else if (o instanceof String){

			}
		}
		return returnList;
	}

	public Relation getRelationTo(String nationName){
		return getRelationTo(NationManager.getInstance().getNationByName(nationName));
	}
	public Relation getRelationTo(Nation otherNation){
		if (otherNation == null){
			return Relation.NEUTRAL;
		}
		if (otherNation == this){
			return Relation.OWN;
		}
		else if (allies.contains(otherNation.getName()) && otherNation.getAllies().contains(name)){
			return Relation.ALLY;
		}
		else if (enemies.contains(otherNation.getName()) || otherNation.getEnemies().contains(name)){
			return Relation.ENEMY;
		} else if (otherNation.getName().equalsIgnoreCase("Warzone")){
			return Relation.WARZONE;
		} else if (otherNation.getName().equalsIgnoreCase("Safezone")){
			return Relation.SAFEZONE;
		}
		else {
			return Relation.NEUTRAL;
		}
	}


	/**
	 *
	 * @return The name of a nation
	 */
	@NotNull public String getName() {
		return name;
	}

	/**
	 * Sets the nation's name
	 * @param name The nation's name
	 */
	public void setName(@NotNull String name) { this.name = name; }

	/**
	 * Returns the description of a nation
	 * @return Nation's description
	 */
	@NotNull public String getDescription() {
		return description;
	}

	public boolean isWarzone(){
		return originalName.equalsIgnoreCase("warzone");
	}

	public boolean isSafezone(){
		return originalName.equalsIgnoreCase("safezone");
	}

	public void setDescription(@NotNull String description){ this.description = description; }

	/**
	 * Get the capital of the nation
	 * @return Nation's capital
	 */
	@NotNull public String getCapital(){ return capital; }

	/**
	 * Set a settlement as a nation's capital
	 * @param capital
	 */
	public void setCapital(@NotNull String capital) { this.capital = capital; }

	@NotNull public List<String> getAllies(){
		return allies;
	}

	public boolean addAlly(String ally){
		return allies.add(ally);
	}

	public boolean removeAlly(String ally){
		return allies.remove(ally);
	}

	@NotNull public List<String> getEnemies(){
		return enemies;
	}

	public boolean addEnemy(String enemy){
		return enemies.add(enemy);
	}

	public boolean removeEnemy(String enemy){
		return enemies.remove(enemy);
	}

	@NotNull public List<String> getSettlements() { return settlements; }

	@NotNull
	public TerritoryManager getTerritoryManager() {
		return territoryManager;
	}

	@NotNull public Map<UUID, Ranks> getPlayers(){
		return players;
	}

	@NotNull public String getOriginalName(){
		return originalName;
	}

	public boolean addPlayer(Player player){
		if (players.containsKey(player.getUniqueId())){
			return false;
		}
		//call event
        NationPlayerJoinEvent event = new NationPlayerJoinEvent(player, this);

		//check if event was cancelled
		if (event.isCancelled()){
		    return false;
        }
		//if not, add the player and assign recruit rank
		players.put(player.getUniqueId(), Ranks.RECRUIT);
		NationCraft.getInstance().getServer().getPluginManager().callEvent(event);
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
	public boolean isStrongEnough(){
		return getStrength() >= getTerritoryManager().size();
	}

	public int getStrength(){
		int strength = 0;
		for (UUID id : players.keySet()){
			strength += PlayerManager.getInstance().getPlayerStrength(id);
		}
		return strength;
	}

	public int getMaxStrength(){
		int maxStrength = 0;
		for (UUID id : players.keySet()){
			maxStrength += Settings.maxStrengthPerPlayer;
		}
		return maxStrength;
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

	@NotNull
	public UUID getUuid(){
		return uuid;
	}
	public Ranks getRankByPlayer(Player p){
		return players.get(p.getUniqueId());
	}

	public boolean isOpen(){
		return isOpen;
	}

	@NotNull
	public Set<UUID> getInvitedPlayers() {
		return invitedPlayers;
	}

	public boolean invite(UUID id){
		invitedPlayers.add(id);
		NationPlayerInviteEvent event = new NationPlayerInviteEvent(this, id);
		NationCraft.getInstance().getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()){
			invitedPlayers.remove(id);
			return false;
		}
		return true;
	}
	public boolean deinvite(UUID id){
		return invitedPlayers.remove(id);
	}
    public boolean equalsFile(){
	    File file = new File(NationCraft.getInstance().getDataFolder(),getUuid().toString() + ".nation");
	    if (!file.exists()){
	        return false;
        }
	    Nation toCompare = new Nation(file);
	    return toCompare.getName() == getName() &&
                toCompare.getDescription() == getDescription() &&
                toCompare.getCapital() == getCapital() &&
                toCompare.getAllies() == getAllies() &&
                toCompare.getEnemies() == getEnemies() &&
                toCompare.getSettlements() == getSettlements() &&
                toCompare.getTerritoryManager() == getTerritoryManager() &&
                toCompare.isOpen() == isOpen() &&
                toCompare.getPlayers() == getPlayers();
	}

	public boolean saveToFile(){
        String path = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
        File f = new File(path);
        if (!f.exists()){
            f.mkdirs();
        }
        path += "/";

        path += getOriginalName();
        path += ".nation";
        f = new File(path);
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            FileWriter writer = new FileWriter(path);
            writer.write("uuid: " + getUuid().toString() + "\n");
            writer.write("name: " + getName() + "\n");
			writer.write("originalName: " + originalName + "\n");
            writer.write("description: " + getDescription() + "\n");
            writer.write("capital: " + getCapital() + "\n");
            writer.write("allies:\n");
            if (!getAllies().isEmpty()) {
            	for (String ally : getAllies()) {
            		writer.write("- " + ally + "\n");
            	}
            }
            writer.write("enemies:\n");
            if (!getEnemies().isEmpty()) {
            	for (String enemy : getEnemies()) {
            		writer.write("- " + enemy + "\n");
            	}
            }
            writer.write("settlements:\n");
            if (!getSettlements().isEmpty()) {
            	for (String settlement : getSettlements()) {
            		writer.write("- " + settlement + "\n");
            	}
            }
            writer.write("territory:\n");
            if (!getTerritoryManager().isEmpty()) {
                for (Territory t : getTerritoryManager()) {
                    writer.write("- [" + t.getWorld().getUID() + ", " + t.getX() + ", " + t.getZ() + "]\n");
                }
            }
            writer.write("isOpen: " + isOpen() + "\n");
            writer.write("invitedPlayers:\n");
            if (getInvitedPlayers().isEmpty()) {
            	for (UUID id : getInvitedPlayers()) {
            		writer.write("- " + id + "\n");
            	}
            }
            writer.write("players:\n");
            for (UUID id : getPlayers().keySet()){
                Ranks r = getPlayers().get(id);
                writer.write("  " + id + ": " + r + "\n");
            }
            writer.close();
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public int compareTo(@NotNull Nation o) {
        int i = 0;
        i += getName().compareToIgnoreCase(o.getName());
        i += getDescription().compareToIgnoreCase(o.getDescription());
        i += getCapital().compareToIgnoreCase(o.getCapital());
		return i;
    }

	@Override
	public int hashCode() {
		return Objects.hash(name, description, capital, allies, enemies, settlements, territoryManager, players, invitedPlayers);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Nation)){
			return false;
		}
		Nation n = (Nation) obj;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		String str = "Nation: \n";
		str += String.format("name: %s \n",name);
		str += String.format("description: %s \n",description);
		str += String.format("capital: %s \n",capital);
		str += String.format("allies: %s \n",allies.toString());
		str += String.format("enemies: %s \n",enemies.toString());
		str += String.format("settlements: %s \n",settlements.toString());
		return str;
	}
}
