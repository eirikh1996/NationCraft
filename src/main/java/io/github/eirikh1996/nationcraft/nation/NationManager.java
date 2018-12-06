package io.github.eirikh1996.nationcraft.nation;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.github.eirikh1996.nationcraft.NationCraft;

public class NationManager {
	private static NationManager ourInstance;
	@NotNull private static Set<Nation> nations = new HashSet<>();

	public NationManager() {
		this.nations = loadNations();
	}

	public Set<Nation> loadNations(){
		File nationFiles = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/nations");
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
		if (nations.isEmpty()) {
			
		}
		return nations;
	}

	@NotNull
	public Set<Nation> getNations() {
		return nations;
	}

	public void initialize() {
		
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

	
}
