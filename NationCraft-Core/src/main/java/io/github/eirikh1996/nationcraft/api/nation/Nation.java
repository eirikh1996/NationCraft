package io.github.eirikh1996.nationcraft.api.nation;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.exception.NationNotFoundException;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.territory.NationTerritoryManager;
import io.github.eirikh1996.nationcraft.core.territory.Territory;
import io.github.eirikh1996.nationcraft.core.territory.TerritoryManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;


final public class Nation implements Comparable<Nation>, Cloneable {
	private final long creationTimeMS;
	@NotNull private final UUID uuid;
	@NotNull private final String originalName;
	@NotNull private String name, description;
	@Nullable private Settlement capital;
	@NotNull private final Set<Nation> allies, enemies, truces;
	@NotNull private final Set<Settlement> settlements;
	@NotNull private final TerritoryManager territoryManager;
	@NotNull private final Map<String, Boolean> flags = new HashMap<>();
	@NotNull private final Map<NCPlayer, Ranks> players;
	@NotNull private final Set<NCPlayer> invitedPlayers = new HashSet<>();
	@NotNull private final Map<NCPlayer, String> bannedPlayers = new HashMap<>();

	public Nation(@NotNull String name, @NotNull String description, @Nullable Settlement capital, @NotNull Set<Nation> allies, @NotNull Set<Nation> enemies, @NotNull Set<Nation> truces, @NotNull Set<Settlement> settlements, @NotNull Map<NCPlayer, Ranks> players) {
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = description;
		this.capital = capital;
		this.allies = allies;
		this.enemies = enemies;
		this.settlements = settlements;
		this.truces = truces;
		this.players = players;
		territoryManager = new NationTerritoryManager(this);
		creationTimeMS = System.currentTimeMillis();
		NationManager.getInstance().getNations().add(this);
	}

	Nation(@NotNull String name, @NotNull String description){
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = description;
		this.capital = null;
		this.allies = new HashSet<>();
		this.enemies = new HashSet<>();
		this.truces = new HashSet<>();
		this.settlements = new HashSet<>();
		this.players = new HashMap<>();
		flags.putAll(NationManager.getInstance().getRegisteredFlags());
		territoryManager = new NationTerritoryManager(this);
		creationTimeMS = System.currentTimeMillis();
		NationManager.getInstance().getNations().add(this);
	}

	public Nation(@NotNull String name, NCPlayer leader){
		this.uuid = UUID.randomUUID();
		this.name = name;
		originalName = name;
		this.description = "Default description";
		this.capital = null;
		this.allies = new HashSet<>();
		this.enemies = new HashSet<>();
		this.truces = new HashSet<>();
		this.settlements = new HashSet<>();
		this.players = new HashMap<>();
		players.put(leader, Ranks.LEADER);
		flags.putAll(NationManager.getInstance().getRegisteredFlags());
		territoryManager = new NationTerritoryManager(this);
		creationTimeMS = System.currentTimeMillis();
		NationManager.getInstance().getNations().add(this);
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
		truces = nationListFromObject("truces");
		settlements = settlementListFromObject("settlements");
		Map<String, Object> flagMap = (Map<String, Object>) data.get("flags");
		for (String key : flagMap.keySet()) {
			flags.put(key, (boolean) flagMap.get(key));
		}
		invitedPlayers.addAll(playerIDListFromObject(data.get("invitedPlayers")));
		Map<String, String> bans = (Map<String, String>) data.getOrDefault("bannedPlayers", new HashMap<>());
		for (String id : bans.keySet()) {
			if (id == null) {
				continue;
			}
			bannedPlayers.put(PlayerManager.getInstance().getPlayer(UUID.fromString(id)), bans.get(id));
		}
		players = getPlayersAndRanksFromObject(data.get("players"));
		territoryManager = new NationTerritoryManager(this);
		territoryManager.addAll(chunkListFromObject(data.get("territory")));
		creationTimeMS = (long) data.get("creationTimeMS");
	}
	private Set<Nation> nationListFromObject(Object obj){
		HashSet<Nation> returnList = new HashSet<>();
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

	private Set<Settlement> settlementListFromObject(Object obj){
	    HashSet<Settlement> returnList = new HashSet<>();
	    if (obj == null) {
	    	return returnList;
		} else if (obj instanceof String){
	        String string = (String) obj;
	        final Settlement fromFile = Settlement.loadFromFile(string);
	        returnList.add(fromFile);
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
	private Set<NCPlayer> playerIDListFromObject(Object obj){
		Set<NCPlayer> returnList = new HashSet<>();
		List<Object> objList = (List<Object>) obj;
		if (objList == null){
			returnList = Collections.emptySet();
		} else {
			for (Object o : objList) {
				String idStr = (String) o;
				returnList.add(PlayerManager.getInstance().getPlayer(UUID.fromString(idStr)));
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
				String world = (String) objects.get(0);
				int x = (Integer) objects.get(1);
				int z = (Integer) objects.get(2);
				returnList.add(new Territory(world, x, z));
			} else if (o instanceof String){
				String str = (String) o;
				str = str.replace("[", "").replace("]", "");
				String[] parts = str.split(",");
				String world = parts[0];
				int x = Integer.parseInt(parts[1]);
				int z = Integer.parseInt(parts[2]);
				returnList.add(new Territory(world, x, z));
			}
		}
		return returnList;
	}

	public void broadcast(@NotNull final String message) {
		for (NCPlayer player : players.keySet()) {
			player.sendMessage(message);
		}
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
		else if (allies.contains(otherNation) && otherNation.getAllies().contains(this)){
			return Relation.ALLY;
		}
		else if (enemies.contains(otherNation) || otherNation.getEnemies().contains(this)){
			return Relation.ENEMY;
		} else if (isWarzone()){
			return Relation.WARZONE;
		} else if (isSafezone()){
			return Relation.SAFEZONE;
		}
		else {
			return Relation.NEUTRAL;
		}
	}


	/**
	 *
	 * Bans a player from the nation. If the banned player is member of the nation, the player
	 * will be removed from the nation
	 *
	 * @param banned The player banned from the nation
	 * @param reason The reason for the ban
	 * @return true if player is currently not banned. False otherwise
	 */
	public boolean ban(NCPlayer banned, String reason) {
		if (bannedPlayers.containsKey(banned)) {
			return false;
		}
		banned.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You have been banned from nation " + name + " for " + reason);
		bannedPlayers.put(banned, reason);
		players.remove(banned);
		return true;
	}

	/**
	 *
	 * Unbans a player from a nation
	 *
	 * @param player the player to unban
	 * @return true if the player is currently banned from the nation, false otherwhise
	 */
	public boolean unban(NCPlayer player) {
		if (!bannedPlayers.containsKey(player)) {
			return false;
		}
		bannedPlayers.remove(player);
		return true;
	}

	/**
	 *
	 * Checks if a player is banned from the nation
	 *
	 * @param player the player that could be banned
	 * @return the ban reason if the player is banned, null otherwhise
	 */
	@Nullable
	public String isBanned(NCPlayer player) {
		return bannedPlayers.getOrDefault(player, null);
	}


	/**
	 * Gets the name of this nation
	 * @return The name of a nation
	 */
	@NotNull public String getName() {
		return name;
	}

	public TextColor getColor(@NotNull NCPlayer player) {
		return getColor(player.getNation());
	}

	public TextColor getColor(@Nullable Nation other) {
		if (isSafezone()){
			return TextColor.GOLD;
		}
		else if (isWarzone()){
			return TextColor.DARK_RED;
		}
		else if (other == null){
			return TextColor.WHITE;
		}
		else if (this.equals(other)){
			return TextColor.GREEN;
		}
		else if (isAlliedWith(other)){
			return TextColor.DARK_PURPLE;
		}
		else if (isAtWarWith(other)){
			return TextColor.RED;
		}
		return TextColor.WHITE;
	}

	public String getName(Nation other){
		String ret = "" + getColor(other);
		ret += getName();
		ret += TextColor.RESET;
		return ret;
	}

	public String getName(UUID playerID) {
		Nation pNation = NationManager.getInstance().getNationByPlayer(playerID);
		return getName(pNation);
	}

	public String getName(NCPlayer player) {;
		return getName(player.getNation());
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
	    for (NCPlayer player : players.keySet()) {
	        if (!player.getPlayerID().equals(id)) {
	            continue;
            }
	        return true;
        }
	    return false;
	}

	public boolean hasPlayer(NCPlayer player){
		return players.containsKey(player);
	}

	/**
	 * Gets if PvP is allowed in this nation's territory
	 * @return true if PvP is allowed, otherwise false
	 */
	public boolean pvpAllowed() {
		return flags.getOrDefault("pvp", true);
	}

	public void setPvPAllowed(boolean state) {
		flags.put("pvp", state);
	}


	/**
	 *
	 * Returns {@code true} if the nation has a capital and {@code false} if not
	 *
	 * @return {@code true} if the nation has a capital and {@code false} if not
	 */
	public boolean hasCapital() {
		return capital != null;
	}

	public boolean monstersAllowed() {
		return flags.getOrDefault("monsters", true);
	}

	public boolean fireSpreadAllowed() {
		return getFlag("firespread");
	}

	public void setMonstersAllowed(boolean state) {
		flags.put("monsters", state);
	}

	public boolean getFlag(String flag) {
		if (!NationManager.getInstance().getRegisteredFlags().containsKey(flag))
			throw new IllegalArgumentException(flag + " is not a valid nation flag");
		return flags.getOrDefault(flag, NationManager.getInstance().getRegisteredFlags().get(flag));
	}

	public boolean hasFlag(String flag) {
		return flags.containsKey(flag);
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
		saveToFile();
	}
	public boolean isWarzone(){
		return flags.getOrDefault("warzone", false);
	}

	public boolean powergainAllowed() {
		return getFlag("powergain");
	}

	public boolean powerlossAllowed() {
		return getFlag("powerloss");
	}

	public void setWarzone(boolean state) {
		flags.put("warzone", state);
	}

	public boolean isSafezone(){
		return flags.getOrDefault("safezone", false);
	}

	public void setSafezone(boolean state) {
		flags.put("safezone", state);
	}

	public void setDescription(@NotNull String description){ this.description = description; }

	/**
	 * Get the capital of the nation
	 * @return Nation's capital
	 *
	 * @see Nation#hasCapital()
	 *
	 */
	@Nullable
	@Contract(pure = true)
	public Settlement getCapital(){ return capital; }

	/**
	 * Set a settlement as a nation's capital
	 * @param capital the settlement to set as nation capital
	 */
	public void setCapital(@Nullable Settlement capital) { this.capital = capital; }

	@NotNull public Set<Nation> getAllies(){
		return allies;
	}

	public boolean addAlly(Nation ally){
		return allies.add(ally);
	}

	public boolean removeAlly(Nation ally){
		return allies.remove(ally);
	}

	public void removePlayer(NCPlayer p){
		if (p.isOnline()) {
			p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You left your nation");
		}
		players.remove(p);
		for (NCPlayer player : players.keySet()){
			if (!player.isOnline()){
				continue;
			}
			player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + p.getName() + " left your nation");
		}
	}

	public void kickPlayer(NCPlayer kicked, NCPlayer kicker){
		broadcast(NATIONCRAFT_COMMAND_PREFIX + kicker.getName() + " kicked " + kicked.getName() + " from the nation");
		players.remove(kicked);
	}

	@NotNull public Set<Nation> getEnemies(){
		return enemies;
	}

	public boolean addEnemy(Nation enemy){
		return enemies.add(enemy);
	}

	public boolean removeEnemy(Nation enemy){
		return enemies.remove(enemy);
	}

	@NotNull public Set<Settlement> getSettlements() { return settlements; }

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
		return isSafezone() || isWarzone() || getPower() >= (double) getTerritoryManager().size();
	}

	public int getPower(){
		int strength = 0;
		for (NCPlayer player : players.keySet()){
			strength += player.getPower();
		}
		return strength;
	}

	public long getCreationTimeMS() {
		return creationTimeMS;
	}

	public double getMaxPower(){
		return Settings.player.MaxPower * players.size();
	}
	public boolean isAlliedWith(Nation alliedNation) {
		return allies.contains(alliedNation) && alliedNation.getAllies().contains(this);
	}

	public boolean isTrucedWith(Nation truce) {
		return truces.contains(truce) && truce.getTruces().contains(this);
	}

	public boolean isAtWarWith(Nation enemyNation) {
		return enemies.contains(enemyNation) || enemyNation.getEnemies().contains(this);
	}

	@NotNull
	public UUID getUuid(){
		return uuid;
	}
	public Ranks getRankByPlayer(NCPlayer p){
		return players.get(p);
	}

	/**
	 *
	 * Gets if a nation is open for other players to join
	 *
	 * NOTE: If a nation is open, players banned from it cannot join.
	 *
	 * @return the open status of the nation
	 */
	public boolean isOpen(){
		return flags.get("open");
	}

	@NotNull
	public Set<NCPlayer> getInvitedPlayers() {
		return invitedPlayers;
	}

	public boolean invite(NCPlayer player){
		invitedPlayers.add(player);
		return true;
	}
	public boolean deinvite(NCPlayer player){
		return invitedPlayers.remove(player);
	}

	public boolean isInvited(NCPlayer player) {
		return !isOpen() && invitedPlayers.contains(player);
	}

	public boolean saveToFile(){
        File f = new File(NationManager.getInstance().getNationDir(), getName().toLowerCase() + ".nation");
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            PrintWriter writer = new PrintWriter(f);
            writer.println("uuid: " + getUuid().toString());
            writer.println("name: " + getName());
            writer.println("creationTimeMS: " + getCreationTimeMS());
			writer.println("originalName: " + originalName);
            writer.println("description: " + getDescription() );
            writer.println("capital: " + (getCapital() != null ? getCapital().getName().toLowerCase() : "" ));
            writer.println("allies:");
            if (!getAllies().isEmpty()) {
            	for (Nation ally : getAllies()) {
            		if (ally == null){
            			continue;
					}
            		writer.println("- " + ally.getName());
            	}
            }
            writer.println("enemies:");
            if (!getEnemies().isEmpty()) {
            	for (Nation enemy : getEnemies()) {
            		if (enemy == null){
            			continue;
					}
            		writer.println("- " + enemy.getName());
            	}
            }
            writer.println("settlements:");
            if (!getSettlements().isEmpty()) {
            	for (Settlement settlement : getSettlements()) {
            		if (settlement == null){
            			continue;
					}
            		writer.println("- " + settlement.getName());
            	}
            }

            writer.println("territory:");
            if (!getTerritoryManager().isEmpty()) {
                for (Territory t : getTerritoryManager()) {
                	if (t == null){
                		continue;
					}
                    writer.println("- [" + t.getWorld() + ", " + t.getX() + ", " + t.getZ() + "]");
                }
            }
            writer.println("flags:");
            for (String flag : flags.keySet()) {
            	writer.println("   " + flag + ": " + flags.get(flag));
			}
            writer.println("invitedPlayers:");
            if (getInvitedPlayers().isEmpty()) {
            	for (NCPlayer player : getInvitedPlayers()) {
            		writer.println("- " + player.getPlayerID());
            	}
            }
            writer.println("players:");
            for (NCPlayer player : getPlayers().keySet()){
                Ranks r = getPlayers().get(player);
                writer.println("   " + player.getPlayerID() + ": " + r );
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
		return uuid.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Nation)){
			return false;
		}
		Nation n = (Nation) obj;
		return uuid.equals(n.uuid);
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

	@NotNull
	public Set<Nation> getTruces() {
		return truces;
	}
}
