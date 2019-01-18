package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;

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
	@Nullable private static Set<Nation> nations = new HashSet<>();

	public NationManager(){
		nations = loadNations();
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

	public boolean saveNationToFile(Nation n){
		String path = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations";
		File f = new File(path);
		if (!f.exists()){
			f.mkdirs();
		}
		path += "/";
		path += n.getName();
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
			writer.write("name: " + n.getName() + "\n");
			writer.write("description: " + n.getDescription() + "\n");
			writer.write("capital: " + n.getCapital() + "\n");
			writer.write("allies:\n");
			for (String ally : n.getAllies()){
				writer.write("- " + ally + "\n");
			}
			writer.write("enemies:\n");
			for (String enemy : n.getEnemies()){
				writer.write("- " + enemy + "\n");
			}
			writer.write("settlements:\n");
			for (String settlement : n.getSettlements()){
				writer.write("- " + settlement + "\n");
			}
			writer.write("territory:\n");
			for (Chunk tc : n.getTerritory()){
				writer.write("- [" + tc.getWorld().getName() + ", " + tc.getX() + ", " + tc.getZ() + "]\n");
			}
			writer.write("isOpen: " + n.isOpen() + "\n");
			writer.write("invitedPlayers:\n");
			for (UUID id : n.getInvitedPlayers()){
				writer.write("- " + id + "\n");
			}

			writer.write("players:\n");
			for (UUID id : n.getPlayers().keySet()){
				Ranks r = n.getPlayers().get(id);
				writer.write("  " + id + ": " + r + "\n");
			}
			writer.close();
			return true;
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}

	}
	public boolean deleteNation(Nation n){
		File nationFile = getNationFile(n.getName());
		if (nations.remove(n)){
			return nationFile.delete();
		}
		return false;
	}

	public ChatColor getColor(Player p, Nation n){
		ChatColor returnColor = ChatColor.RESET;
		Nation pNation = NationManager.getInstance().getNationByPlayer(p);
		if (pNation == n){
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
		return returnColor;
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
