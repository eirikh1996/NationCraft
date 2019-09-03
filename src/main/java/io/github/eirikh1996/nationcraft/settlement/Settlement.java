package io.github.eirikh1996.nationcraft.settlement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.territory.SettlementTerritoryManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import io.github.eirikh1996.nationcraft.territory.TerritoryManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

final public class Settlement {
	private String name;
	private final World world;
	private Nation nation;
	private Territory townCenter;
	private final TerritoryManager territory;
	private final List<UUID> players;
	
	public Settlement(File settlementFile) {
		final Map data;
		try {
			InputStream input = new FileInputStream(settlementFile);
			Yaml yaml = new Yaml();
			data = (Map) yaml.load(input);
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new SettlementNotFoundException("Settlement file at " + settlementFile.getAbsolutePath() + " was not found");
		}
			name = (String) data.get("name");
			nation = NationManager.getInstance().getNationByName((String) data.get("nation"));
			world = worldFromObject(data.get("world"));
			townCenter = (Territory) data.get("townCenter");
			territory = new SettlementTerritoryManager(this);
			players = (List<UUID>) data.get("players");
		
	}
	public Settlement(String name, Nation nation, World world, Territory townCenter, List<UUID> players) {
		this.name = name;
		this.nation = nation;
		this.world = world;
		this.townCenter = townCenter;
		this.players = players;
		this.territory = new SettlementTerritoryManager(this);
	}
	private World worldFromObject(Object obj){
		World  returnWorld = null;
		UUID id = null;
		if (obj instanceof UUID){
			id = (UUID) obj;
		} else if (obj instanceof String){
			String str = (String) obj;
			id = UUID.fromString(str);
		}
		if (id != null){
			returnWorld = Bukkit.getServer().getWorld(id);
		}
		return returnWorld;
	}
	private Set<Territory> chunkListFromObject(Object obj){
		Set<Territory> returnList = new HashSet<>();
		List<Object> objList = (List<Object>) obj;
		if (objList == null){
			return Collections.emptySet();
		}
		for (Object o : objList){
			if (o instanceof ArrayList){
				List<?> objects = (List<?>) o;
				String wName = (String) objects.get(0);
				int x = (Integer) objects.get(1);
				int z = (Integer) objects.get(2);
				World world = Bukkit.getWorld(wName);
				returnList.add(new Territory(world, x, z));
			} else if (o instanceof String){

			}
		}
		return returnList;
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
	public void setTownCenter(Territory townCenter) {
		this.townCenter = townCenter;
	}

	
	public boolean hasTownCenter() {
		if (townCenter == null) {
			return false;
		} else {
			return true;
		}
	}

	public void siege(){

	}
	public String getName() {
		return name;
	}
	
	public List<UUID> getPlayers() {
		return players;
	}
	
	public TerritoryManager getTerritory() {
		return territory;
	}
	
	public Territory getTownCenter() {
		return townCenter;
	}

	public World getWorld(){
		return world;
	}

	public Nation getNation(){
		return nation;
	}

	private class SettlementNotFoundException extends RuntimeException{
		public SettlementNotFoundException(String s){
			super(s);
		}
	}
}
