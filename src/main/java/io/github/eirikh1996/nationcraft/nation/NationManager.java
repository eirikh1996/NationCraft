package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.jetbrains.annotations.Nullable;

import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationManager extends BukkitRunnable implements Iterable<Nation> {
	private static NationManager ourInstance;
	private Map<String, Boolean> registeredFlags = new HashMap<>();
	private String nationFilePath = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
	@NotNull private final Set<Nation> nations;

	public NationManager(){
		nations = new HashSet<>();
		registeredFlags.put("pvp", true);
		registeredFlags.put("monsters", true);
		registeredFlags.put("open", false);
		registeredFlags.put("safezone", false);
		registeredFlags.put("warzone", false);
	}

	public static void initialize(){
		ourInstance = new NationManager();
		ourInstance.runTaskTimerAsynchronously(NationCraft.getInstance(), 0, 1);
	}

	public void loadNations(){
		nations.addAll(getNationsFromFile());
	}

	public Map<String, Boolean> getRegisteredFlags() {
		return registeredFlags;
	}

	/**
	 * Registers a new nation flag
	 *
	 * This is preferred to be done at onLoad in your plugin's main class
	 * @param flag The identifier of the flag
	 * @param def the default value of the flag
	 */
	public void registerFlag(String flag, boolean def) {
		registeredFlags.put(flag, def);
	}

	public boolean registeredFlag(String flag) {
		return registeredFlags.containsKey(flag);
	}

	public void reload(){
		nations.clear();
	    nations.addAll(getNationsFromFile());
    }

	public HashSet<Nation> getNationsFromFile(){
		File nationFiles = new File(nationFilePath);
		if (!nationFiles.exists()){
			nationFiles.mkdirs();
		}
		HashSet<Nation> nations = new HashSet<>();
		File[] files = nationFiles.listFiles();
		if (files == null) {
			return nations;
		}

		for (File file : files) {
			if (!file.isFile()) {
				continue;
			}
			if (!file.getName().contains(".nation")) {
				continue;
			}
			Nation n = new Nation(file);
			nations.add(n);
		}
        NationCraft.getInstance().getLogger().info("Nations loaded: ");
		for (Nation nation : nations){
		    NationCraft.getInstance().getLogger().info(nation.getName());
        }
		return nations;
	}
	public Nation getNationBySettlement(Settlement settlement){
		for (Nation n : this){
			if (n.getSettlements().contains(settlement) || n.getCapital() != null && n.getCapital().equals(settlement)){
				return n;
			}
		}
		return null;
	}
	public Nation getNationAt(Chunk chunk){
		Nation returnNation = null;
		for (Nation nation : nations){
			for (Territory terr : nation.getTerritoryManager()){
				if (!terr.isTerritory(chunk))
					continue;
				returnNation = nation;
				break;
			}
			if (returnNation == nation)
				break;
		}
		return returnNation;
	}

	public Nation getNationAt(Location location){
		Nation returnNation = null;
		for (Nation nation : nations){
			for (Territory terr : nation.getTerritoryManager()){
				if (!terr.isTerritory(location.getChunk()))
					continue;
				returnNation = nation;
				break;
			}
			if (returnNation == nation)
				break;
		}
		return returnNation;
	}

	public Nation getNationAt(Territory territory){
		Nation returnNation = null;
		for (Nation nation : this){
			if (nation.getTerritoryManager().contains(territory)){
				returnNation = nation;
				break;
			}
		}
		return returnNation;
	}

	public Nation getNationByPlayer(UUID id){
		for (Nation nation : nations){
			if (!nation.hasPlayer(id)){
				continue;
			}
			return nation;
		}
		return null;
	}
	public Nation getNationAt(World world, int x, int z){
		Nation returnNation = null;
		for (Nation nation : nations){
			for (Territory terr : nation.getTerritoryManager()){
				if (terr.getX() != x||terr.getZ() != z || terr.getWorld() != world)
					continue;
				returnNation = nation;
				break;
			}
			if (returnNation == nation)
				break;
		}
		return returnNation;
	}

	public File getNationFile(String nationName){
		return new File(nationFilePath + "/" + nationName.toLowerCase() + ".nation");
	}

	public Set<Nation> getNations() {
		return nations;
	}

	public Nation getNationByName(String name){
		Nation returnNation = null;
		for (Nation n : nations){
			if (n.getName().equalsIgnoreCase(name)){
				returnNation = n;
			}
		}
		return returnNation;
	}




	public ChatColor getColor(@NotNull Player p, @NotNull Nation n){
		ChatColor returnColor = ChatColor.RESET;
		Nation pNation = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
		if (pNation == null){
		    returnColor = ChatColor.WHITE;
        }
		else if (pNation == n){
			returnColor = ChatColor.GREEN;
		}
		else if (pNation.getRelationTo(n) == Relation.ENEMY){
			returnColor = ChatColor.RED;
		}
		else if (pNation.getRelationTo(n) == Relation.ALLY){
			returnColor = ChatColor.DARK_PURPLE;
		}
		else if (pNation.getRelationTo(n) == Relation.NEUTRAL){
		    returnColor = ChatColor.WHITE;
		}
		if (n.getOriginalName().equalsIgnoreCase("Warzone")){
		    returnColor = ChatColor.DARK_RED;
        } else if (n.getOriginalName().equalsIgnoreCase("Safezone")){
		    returnColor = ChatColor.GOLD;
        }
		return returnColor;
	}

	public boolean createSafezone(){

		Nation safezone = new Nation("Safezone","Free from PvP and monsters", null,Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyMap());
		safezone.setSafezone(true);
		safezone.setPvPAllowed(false);
		safezone.setMonstersAllowed(false);
		return safezone.saveToFile();
	}

	public boolean createWarzone(){

		Nation warzone = new Nation("Warzone","Not the safest place to be!", null,Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyMap());
		warzone.setWarzone(true);
		return warzone.saveToFile();
	}

	public static NationManager getInstance(){
		return ourInstance;
	}

	public void saveAllNationsToFile(){
		for (Nation n : this){
			if (n == null || n.getPlayers().isEmpty() && !n.isWarzone() && !n.isSafezone()){
				continue;
			}
			n.saveToFile();
		}
	}
	public String getNationFilePath(){
		return nationFilePath;
	}


	@NotNull
	@Override
	public Iterator<Nation> iterator() {
		return Collections.unmodifiableSet(this.nations).iterator();
	}


    @Override
    public void run() {
        processNationCleanup();
    }

    private void processNationCleanup() {
	    if (nations.isEmpty())
	        return;
	    final Iterator<Nation> iterator = nations.iterator();
	    while (iterator.hasNext()) {
	        final Nation n = iterator.next();
	        if (n == null || !n.getPlayers().isEmpty() || n.isSafezone() || n.isWarzone()) {
	            continue;
            }

	        final File nFile = getNationFile(n.getName());
            Bukkit.broadcastMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("Nation %s has been disbanded", n.getName()));
	        nFile.delete();
	        iterator.remove();

        }
    }
}
