package io.github.eirikh1996.nationcraft.nation;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
	@NotNull private Set<Nation> nations;

	public NationManager(){
		this.nations = loadNations();
	}
	public static void initialize(){ ourInstance = new NationManager();}

	public void reload(){
	    this.nations = loadNations();
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
			NationCraft.getInstance().getLogger().info(n.getName());
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
			if (nation.getTerritory().contains(chunk)){
				returnNation = nation;
			}
		}
		return returnNation;
	}

	public Nation getNationAt(Location location){
		Nation returnNation = null;
		for (Nation nation : nations){
			if (nation.getTerritory().contains(location.getChunk()))
				returnNation = nation;
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
			if (n.getAllies() != null) {
				if (!n.getAllies().isEmpty()) {
					for (String ally : n.getAllies()) {
						writer.write("- " + ally + "\n");
					}
				}
			}
			writer.write("enemies:\n");
			if (n.getEnemies() != null) {
				if (!n.getEnemies().isEmpty()) {
					for (String enemy : n.getEnemies()) {
						writer.write("- " + enemy + "\n");
					}
				}
			}
			writer.write("settlements:\n");
			if (n.getSettlements() != null) {
				if (!n.getSettlements().isEmpty()) {
					for (String settlement : n.getSettlements()) {
						writer.write("- " + settlement + "\n");
					}
				}
			}
			writer.write("territory:\n");
			if (n.getTerritory() != null) {
				if (!n.getTerritory().isEmpty()) {
					for (Chunk tc : n.getTerritory()) {
						writer.write("- [" + tc.getWorld().getUID() + ", " + tc.getX() + ", " + tc.getZ() + "]\n");
					}
				}
			}
			writer.write("isOpen: " + n.isOpen() + "\n");
			writer.write("invitedPlayers:\n");
			if (n.getInvitedPlayers() != null) {
				if (n.getInvitedPlayers().isEmpty()) {

					for (UUID id : n.getInvitedPlayers()) {
						writer.write("- " + id + "\n");
					}
				}
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

	public ChatColor getColor(@NotNull Player p,@NotNull Nation n){
		ChatColor returnColor = ChatColor.RESET;
		Nation pNation = NationManager.getInstance().getNationByPlayer(p);
		if (pNation == null){
		    return ChatColor.WHITE;
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
			if (n.getName().equalsIgnoreCase("safezone")){
				returnColor = ChatColor.GOLD;
			} else if (n.getName().equalsIgnoreCase("warzone")){
				returnColor = ChatColor.DARK_RED;
			} else {
				returnColor = ChatColor.WHITE;
			}
		}
		return returnColor;
	}

	public boolean createSafezone(){

		Nation safezone = new Nation("Safezone","Free from PvP and monsters", "(none)",Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptySet(),Collections.emptyMap());
		return saveNationToFile(safezone);
	}

	public boolean createWarzone(){

		Nation safezone = new Nation("Warzone","Not the safest place to be!", "(none)",Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptySet(),Collections.emptyMap());
		return saveNationToFile(safezone);
	}

	public static NationManager getInstance(){
		return ourInstance;
	}

	public String getNationFilePath(){
		return nationFilePath;
	}

	public void saveAllNations(){
		for (Nation n : nations){
			saveNationToFile(n);
		}
	}
	@NotNull
	@Override
	public Iterator<Nation> iterator() {
		return Collections.unmodifiableSet(this.nations).iterator();
	}
}
