package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.jetbrains.annotations.Nullable;

public class NationManager implements Iterable<Nation> {
	private static NationManager ourInstance;
	private boolean fileCreated;
	private String nationFilePath = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
	@NotNull private final Set<Nation> nations;

	public NationManager(){
		this.nations = loadNations();
	}
	public static void initialize(){ ourInstance = new NationManager(); }

	public void reload(){
		nations.clear();
	    nations.addAll(loadNations());
    }

	public HashSet<Nation> loadNations(){
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

	public boolean nationDataChanged(Nation nation){
		File nationFile = getNationFile(nation.getName());
		if (!nationFile.exists()){
			return true;
		}
		Nation fileNation = new Nation(nationFile);
		if (fileNation == null){
			return true;
		}
		return fileNation != nation;
	}

	public File getNationFile(String nationName){
		return new File(nationFilePath + "/" + nationName + ".nation");
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

	public Nation getNationByPlayer(Player p){
		Nation returnNation = null;
		for (Nation n : nations){
			if (n.getPlayers().containsKey(p.getUniqueId())){
				returnNation = n;
			}
		}
		return returnNation;
	}


	public boolean deleteNation(Nation n){
		File nationFile = getNationFile(n.getName());
		if (nations.remove(n)){
			return nationFile.delete();
		}
		return false;
	}

	public ChatColor getColor(@NotNull Player p, @NotNull Nation n){
		ChatColor returnColor = ChatColor.RESET;
		Nation pNation = NationManager.getInstance().getNationByPlayer(p);
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

		Nation safezone = new Nation("Safezone","Free from PvP and monsters", "(none)",Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyMap());
		return safezone.saveToFile();
	}

	public boolean createWarzone(){

		Nation safezone = new Nation("Warzone","Not the safest place to be!", "(none)",Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyMap());
		return safezone.saveToFile();
	}

	public static NationManager getInstance(){
		return ourInstance;
	}

	public void saveAllNationsToFile(){
		for (Nation n : this){
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

}
