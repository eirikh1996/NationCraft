package io.github.eirikh1996.nationcraft.nation;

import java.io.File;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.jetbrains.annotations.Nullable;

public class NationManager implements Iterable<Nation> {
	private static NationManager ourInstance;
	private BukkitTask saveTask;
	private String nationFilePath = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
	@Nullable private static Set<Nation> nations = new HashSet<>();

	public NationManager(){
		nations = loadNations();
		saveTask = new NationSaveTask().runTaskTimerAsynchronously(NationCraft.getInstance(), 0,1);
	}
	public static void initialize(){ ourInstance = new NationManager();}

	public Set<Nation> loadNations(){
		File nationFiles = new File(nationFilePath);
		if (!nationFiles.exists()){
			nationFiles.mkdirs();
		}
		Set<Nation> nations = new HashSet<>();
		File[] files = nationFiles.listFiles();
		if (files == null) {
			return nations;
		}

		for (File file : files) {
			if (file.isFile()) {
				if (file.getName().contains(".nation")) {
					Nation n = new Nation(file);
					nations.add(n);
				}
			}
		}
		if (nations.isEmpty()){
			NationCraft.getInstance().getLogger().info("No nation files found.");
		} else {
			NationCraft.getInstance().getLogger().info(String.format("Loaded %d Nation files", nations.size()));
		}
		return nations;
	}
	public Nation getNationAt(Chunk chunk){
		Nation returnNation = null;
		for (Nation nation : nations){
			returnNation = nation.getTerritory().contains(chunk) ? nation : null;
		}
		return returnNation;
	}

	public Nation getNationAt(Location location){
		Nation returnNation = null;
		for (Nation nation : nations){
			returnNation = nation.getTerritory().contains(location.getChunk()) ? nation : null;
		}
		return returnNation;
	}

	public Nation getNationAt(int x, int z){
		Nation returnNation = null;
		for (Nation nation : nations){
			for (World world : Bukkit.getWorlds()) {
				if (nation.getTerritory().contains(world.getChunkAt(x, z))){
					returnNation = nation;
					break;
				}
			}
		}
		return returnNation;
	}

	public boolean nationDataChanged(Nation nation){
		File nationFile = getNationFile(nation.getName());
		Nation fileNation = new Nation(nationFile);
		if (fileNation == null){
			return true;
		}
		return nation.getName() != fileNation.getName() ||
				nation.getAllies() != fileNation.getAllies()||
				nation.getEnemies() != fileNation.getEnemies() ||
				nation.getDescription() != fileNation.getDescription() ||
				nation.getCapital() != fileNation.getCapital() ||
				nation.getPlayers() != fileNation.getPlayers() ||
				nation.getTerritory() != fileNation.getTerritory();
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
			if (n.hasPlayer(p)){
				returnNation = n;
			}
		}
		return returnNation;
	}

	public void saveAllNationsToFile(){
		for (Nation n : nations){
			n.saveNationToFile();
		}
	}

	public static NationManager getInstance(){
		return ourInstance;
	}

	public String getNationFilePath(){
		return nationFilePath;
	}

	@NotNull
	@Override
	public Iterator<Nation> iterator() {
		return Collections.unmodifiableSet(nations).iterator();
	}
}
