package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerInviteEvent;
import io.github.eirikh1996.nationcraft.events.nation.NationPlayerJoinEvent;
import io.github.eirikh1996.nationcraft.exception.NationNotFoundException;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.territory.NationTerritoryManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import io.github.eirikh1996.nationcraft.territory.TerritoryManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;


final public class Nation implements Comparable<Nation>, Cloneable {
	@NotNull private final long creationTimeMS;
	@NotNull private final UUID uuid;
	@NotNull private final String originalName;
	@NotNull private String name, description;
	@Nullable private Settlement capital;
	@NotNull private final List<Nation> allies, enemies;
	@NotNull private final List<Settlement> settlements;
	@NotNull private final TerritoryManager territoryManager;
	@NotNull private final Map<String, Boolean> flags = new HashMap<>();
	@NotNull private final Map<NCPlayer, Ranks> players;
	@NotNull private final Set<UUID> invitedPlayers;

	public Nation(@NotNull String name, @NotNull String description, @Nullable Settlement capital, @NotNull List<Nation> allies, @NotNull List<Nation> enemies, @NotNull List<Settlement> settlements, @NotNull Map<NCPlayer, Ranks> players) {
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = description;
		this.capital = capital;
		this.allies = allies;
		this.enemies = enemies;
		this.settlements = settlements;
		this.players = players;
		invitedPlayers = new HashSet<>();
		territoryManager = new NationTerritoryManager(this);
		creationTimeMS = System.currentTimeMillis();
	}

	public Nation(@NotNull String name, Player leader){
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = "Default description";
		this.capital = null;
		this.allies = new ArrayList<>();
		this.enemies = new ArrayList<>();
		this.settlements = new ArrayList<>();
		this.players = new HashMap<>();
		players.put(PlayerManager.getInstance().getPlayer(leader.getUniqueId()), Ranks.LEADER);
		flags.putAll(NationManager.getInstance().getRegisteredFlags());
		invitedPlayers = new HashSet<>();
		territoryManager = new NationTerritoryManager(this);
		creationTimeMS = System.currentTimeMillis();
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
		capital = Settlement.loadFromFile((String) data.get("capital"));
		allies = nationListFromObject("allies");
		enemies = nationListFromObject("enemies");
		settlements = settlementListFromObject("settlements");
		Map<String, Object> flagMap = (Map<String, Object>) data.get("flags");
		for (String key : flagMap.keySet()) {
			flags.put(key, (boolean) flagMap.get(key));
		}
		invitedPlayers = playerIDListFromObject(data.get("invitedPlayers"));
		players = getPlayersAndRanksFromObject(data.get("players"));
		territoryManager = new NationTerritoryManager(this);
		territoryManager.addAll(chunkListFromObject(data.get("territory")));
		creationTimeMS = (long) data.get("creationTimeMS");
	}
	private ArrayList<Nation> nationListFromObject(Object obj){
		ArrayList<Nation> returnList = new ArrayList<>();
		if (obj == null){
			return returnList;
		} else if (obj instanceof ArrayList) {
			List<Object> objList = (List<Object>) obj;
			for (Object o : objList) {
				if (o instanceof String) {
					String str = (String) o;
					Nation ally = NationManager.getInstance().getNationByName(str);
					if (ally == null){
						return returnList;
					}
					returnList.add(ally);
				}
			}
		} else if (obj instanceof String){
			String string = (String) obj;
			if (string.length() == 0){
				return returnList;
			}
			Nation ally = NationManager.getInstance().getNationByName(string);
			if (ally == null){
				return returnList;
			}
			returnList.add(ally);
		}
		return returnList;
	}

	private ArrayList<Settlement> settlementListFromObject(Object obj){
	    ArrayList<Settlement> returnList = new ArrayList<>();
	    if (obj instanceof String){
	        String string = (String) obj;
	        returnList.add(Settlement.loadFromFile(string));
        } else if (obj instanceof ArrayList){
	        ArrayList objList = (ArrayList) obj;
	        for (Object o : objList){
	            if (!(o instanceof String)){
	                continue;
                }
	            String string = (String) o;
	            returnList.add(Settlement.loadFromFile(string));
            }
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
	private Map<NCPlayer, Ranks> getPlayersAndRanksFromObject(Object obj){
		HashMap<NCPlayer, Ranks> returnMap = new HashMap<>();
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
			final NCPlayer player = PlayerManager.getInstance().getPlayer(id);
			Object i = objMap.get(o);
			if (i instanceof Ranks) {
				Ranks r = (Ranks) i;
				returnMap.put(player, r);
			} else if (i instanceof String){
				String str = (String) i;
				Ranks r = Ranks.valueOf(str);
				returnMap.put(player, r);

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
		else if (allies.contains(otherNation) && otherNation.getAllies().contains(name)){
			return Relation.ALLY;
		}
		else if (enemies.contains(otherNation) || otherNation.getEnemies().contains(name)){
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

	public String getName(Nation other){
		String ret = "" + ChatColor.WHITE;
		if (other == null){
			ret += ChatColor.WHITE;
		}
		else if (this.equals(other)){
			ret += ChatColor.GREEN;
		}
		else if (isAlliedWith(other)){
			ret += ChatColor.DARK_PURPLE;
		}
		else if (isAtWarWith(other)){
			ret += ChatColor.RED;
		}
		else if (isSafezone()){
			ret += ChatColor.GOLD;
		}
		else if (isWarzone()){
			ret += ChatColor.DARK_RED;
		}
		ret += getName();
		ret += ChatColor.RESET;
		return ret;
	}

	public String getName(UUID playerID) {
		Nation pNation = NationManager.getInstance().getNationByPlayer(playerID);
		return getName(pNation);
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

	public boolean hasPlayer(UUID id){
		return players.containsKey(PlayerManager.getInstance().getPlayer(id));
	}

	public boolean pvpAllowed() {
		return flags.get("pvp");
	}

	public void setPvPAllowed(boolean state) {
		flags.put("pvp", state);
	}

	public boolean monstersAllowed() {
		return flags.get("monsters");
	}

	public void setMonstersAllowed(boolean state) {
		flags.put("monsters", state);
	}

	public boolean getFlag(String flag) {
		if (!flags.containsKey(flag))
			throw new IllegalArgumentException(flag + " is not a valid nation flag");
		return flags.get(flag);
	}

	/**
	 *
	 * @param flag
	 * @param state
	 *
	 * @throws IllegalArgumentException if flag is not registered
	 */
	public void setFlag(String flag, boolean state) {
		if (!NationManager.getInstance().registeredFlag(flag)) {
			throw new IllegalArgumentException("Nation flag " + flag + " is not registered");
		}
		flags.put(flag, state);
	}
	public boolean isWarzone(){
		return flags.get("warzone");
	}

	public void setWarzone(boolean state) {
		flags.put("warzone", state);
	}

	public boolean isSafezone(){
		return flags.get("safezone");
	}

	public void setSafezone(boolean state) {
		flags.put("safezone", state);
	}

	public void setDescription(@NotNull String description){ this.description = description; }

	/**
	 * Get the capital of the nation
	 * @return Nation's capital
	 */
	@Nullable public Settlement getCapital(){ return capital; }

	/**
	 * Set a settlement as a nation's capital
	 * @param capital
	 */
	public void setCapital(@Nullable Settlement capital) { this.capital = capital; }

	@NotNull public List<Nation> getAllies(){
		return allies;
	}

	public boolean addAlly(Nation ally){
		return allies.add(ally);
	}

	public boolean removeAlly(Nation ally){
		return allies.remove(ally);
	}

	public void removePlayer(UUID uuid){
		final NCPlayer p = PlayerManager.getInstance().getPlayer(uuid);
		players.remove(p);
		for (NCPlayer player : players.keySet()){
			Player np = Bukkit.getPlayer(player.getPlayerID());
			if (np == null){
				continue;
			}
			np.sendMessage(NATIONCRAFT_COMMAND_PREFIX + p.getName() + " left your nation");
		}
		if (players.size() == 0){
			NationManager.getInstance().deleteNation(this);
		}
	}

	public void kickPlayer(Player kicked, Player kicker){
		players.remove(kicked.getUniqueId());
		for (NCPlayer player : players.keySet()){
			Player np = Bukkit.getPlayer(player.getPlayerID());
			if (np == null){
				continue;
			}
			np.sendMessage(NATIONCRAFT_COMMAND_PREFIX + kicker.getName() + " kicked " + kicked.getName() + " from the nation");
		}
	}

	@NotNull public List<Nation> getEnemies(){
		return enemies;
	}

	public boolean addEnemy(Nation enemy){
		return enemies.add(enemy);
	}

	public boolean removeEnemy(Nation enemy){
		return enemies.remove(enemy);
	}

	@NotNull public List<Settlement> getSettlements() { return settlements; }

	@NotNull
	public TerritoryManager getTerritoryManager() {
		return territoryManager;
	}

	@NotNull public Map<NCPlayer, Ranks> getPlayers(){
		return players;
	}

	@NotNull public String getOriginalName(){
		return originalName;
	}

	public boolean addPlayer(NCPlayer player){
		if (players.containsKey(player)){
			return false;
		}
		//call event
        NationPlayerJoinEvent event = new NationPlayerJoinEvent(player, this);
		Bukkit.getServer().getPluginManager().callEvent(event);
		//check if event was cancelled
		if (event.isCancelled()){
		    return false;
        }
		//if not, add the player and assign recruit rank
		players.put(player, Ranks.RECRUIT);
		return true;
	}

	public boolean promotePlayer(NCPlayer p){
	    final Ranks origRank = players.get(p);
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
        if (!players.containsKey(p)){
            return false;
        }
        players.put(p, newRank);
        return true;
    }

    public boolean demotePlayer(NCPlayer p){
        final Ranks origRank = players.get(p);
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
        if (!players.containsKey(p)){
            return false;
        }
        players.put(p, newRank);
        return true;
    }

	public boolean isStrongEnough(){
		return getPower() >= (double) getTerritoryManager().size();
	}

	public double getPower(){
		double strength = 0.0;
		for (NCPlayer player : players.keySet()){
			strength += player.getPower();
		}
		return strength;
	}

	public long getCreationTimeMS() {
		return creationTimeMS;
	}

	public double getMaxPower(){
		return Settings.PlayerMaxPower * players.size();
	}
	public boolean isAlliedWith(Nation alliedNation) {
		return allies.contains(alliedNation) && alliedNation.getAllies().contains(this);
	}

	public boolean isAtWarWith(Nation enemyNation) {
		return enemies.contains(enemyNation) || enemyNation.getEnemies().contains(this);
	}

	@NotNull
	public UUID getUuid(){
		return uuid;
	}
	public Ranks getRankByPlayer(Player p){
		return players.get(p.getUniqueId());
	}

	public boolean isOpen(){
		return flags.get("open");
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

	public boolean saveToFile(){
        String path = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
        File f = new File(path);
        if (!f.exists()){
            f.mkdirs();
        }
        path += "/";

        path += getName().toLowerCase();
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
            writer.write("creationTimeMS: " + getCreationTimeMS() + "\n");
			writer.write("originalName: " + originalName + "\n");
            writer.write("description: " + getDescription() + "\n");
            writer.write("capital: " + (getCapital() != null ? getCapital().getName().toLowerCase() : "" ) + "\n");
            writer.write("allies:\n");
            if (!getAllies().isEmpty()) {
            	for (Nation ally : getAllies()) {
            		if (ally == null){
            			continue;
					}
            		writer.write("- " + ally.getName() + "\n");
            	}
            }
            writer.write("enemies:\n");
            if (!getEnemies().isEmpty()) {
            	for (Nation enemy : getEnemies()) {
            		if (enemy == null){
            			continue;
					}
            		writer.write("- " + enemy.getName() + "\n");
            	}
            }
            writer.write("settlements:\n");
            if (!getSettlements().isEmpty()) {
            	for (Settlement settlement : getSettlements()) {
            		if (settlement == null){
            			continue;
					}
            		writer.write("- " + settlement.getName() + "\n");
            	}
            }
            writer.write("territory:\n");
            if (!getTerritoryManager().isEmpty()) {
                for (Territory t : getTerritoryManager()) {
                	if (t == null){
                		continue;
					}
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
            for (NCPlayer player : getPlayers().keySet()){
                Ranks r = getPlayers().get(player);
                writer.write("  " + player.getPlayerID() + ": " + r + "\n");
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
        i += getCapital().getName().compareToIgnoreCase(o.getCapital().getName());
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
		str += String.format("capital: %s \n",capital != null ? capital.getName() : "null");
		str += String.format("allies: %s \n",allies.toString());
		str += String.format("enemies: %s \n",enemies.toString());
		str += String.format("settlements: %s \n",settlements.toString());
		return str;
	}
}
