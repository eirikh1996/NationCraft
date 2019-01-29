package io.github.eirikh1996.nationcraft.settlement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

public class Settlement {
	private static String name;
	private static Chunk townCenter;
	private static LinkedList<Chunk> territory;
	private static List<UUID> players;
	
	public Settlement(File settlementFile) {
		Map data = new HashMap();
		try {
			InputStream input = new FileInputStream(settlementFile);
			Yaml yaml = new Yaml();
			data = (Map) yaml.load(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			name = (String) data.get("name");
			townCenter = (Chunk) data.get("townCenter"); 
			territory = (LinkedList<Chunk>) data.get("territory");
			players = (List<UUID>) data.get("players");
		
	}
	public Settlement(String name, Chunk townCenter, LinkedList<Chunk> territory, List<UUID> players) {
		this.name = name;
		this.townCenter = townCenter;
		this.territory = territory;
		this.players = players;
	}
	
	public Settlement() {
		// TODO Auto-generated constructor stub
	}

	
	public void addPlayer(Player p) {
		players.add(p.getUniqueId());
	}
	
	public void removePlayer(Player p) {
		players.remove(p.getUniqueId());
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@SuppressWarnings("static-access")
	public void setTownCenter(Chunk townCenter) {
		this.townCenter = townCenter;
	}
	
	@SuppressWarnings("static-access")
	public void addTerritory(Chunk territory) {
		this.territory.add(territory);
	}
	
	public void removeTerritory(Chunk territory) {
		this.territory.remove(territory);
	}
	
	public boolean hasTownCenter() {
		if (townCenter == null) {
			return false;
		} else {
			return true;
		}
	}
	public String getName() {
		return name;
	}
	
	public List<UUID> getPlayers() {
		return players;
	}
	
	public List<Chunk> getTerritory() {
		return territory;
	}
	
	public Chunk getTownCenter() {
		return townCenter;
	}
}
