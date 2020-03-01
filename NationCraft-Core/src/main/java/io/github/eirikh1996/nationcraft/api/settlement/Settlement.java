package io.github.eirikh1996.nationcraft.api.settlement;

import java.io.*;
import java.util.*;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.territory.SettlementTerritoryManager;
import io.github.eirikh1996.nationcraft.core.territory.Territory;
import io.github.eirikh1996.nationcraft.core.territory.TerritoryManager;
import io.github.eirikh1996.nationcraft.core.territory.TownCenter;
import io.github.eirikh1996.nationcraft.core.utils.Direction;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

final public class Settlement {
	private String name;
	private final UUID world;
	private String nation;
	private TownCenter townCenter;
	private final TerritoryManager territory;
	private final HashMap<NCPlayer, Ranks> players;
	private boolean underSiege = false;
	
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
			nation = (String) data.get("nation");
			world = uuidFromObject(data.get("world"));
			Map<String, Object> tcData = (Map<String, Object>) data.get("townCenter");
			final int tcX = (int) tcData.get("x");
			final int tcZ = (int) tcData.get("z");
			final ArrayList<Integer> tpCoords = (ArrayList<Integer>) tcData.get("teleportationPoint");
			tpCoords.size();
			UUID world = UUID.fromString((String) tcData.get("world"));
			final NCLocation teleportLoc = new NCLocation(world, tpCoords.get(0), tpCoords.get(1), tpCoords.get(2));
			townCenter = new TownCenter(tcX, tcZ, world, teleportLoc);
			territory = new SettlementTerritoryManager(this);
			ArrayList<ArrayList> terrList = (ArrayList<ArrayList>) data.get("territory");
			for (ArrayList list : terrList){
				territory.add(new Territory(UUID.fromString((String) list.get(0)), (int) list.get(1), (int) list.get(2)));
			}
			players = new HashMap<>();
			Map playerData = (Map) data.get("players");
			for (Object obj : playerData.keySet()){
				UUID id = UUID.fromString((String) obj);
				Ranks rank;
				Object val = playerData.get(obj);
				if (val instanceof String){
					rank = Ranks.valueOf((String) val);
				} else {
					rank = (Ranks) val;
				}
				players.put(PlayerManager.getInstance().getPlayer(id), rank);
			}
		
	}
	public Settlement(String name, String nation, UUID world, TownCenter townCenter, HashMap<NCPlayer, Ranks> players) {
		this.name = name;
		this.nation = nation;
		this.world = world;
		this.townCenter = townCenter;
		this.players = players;
		this.territory = new SettlementTerritoryManager(this);
	}
	public Settlement(String name, NCPlayer creator){
		this.name = name;
		nation = NationManager.getInstance().getNationByPlayer(creator.getPlayerID()).getName();
		world = creator.getLocation().getWorld();
		townCenter = new TownCenter(creator.getLocation().getBlockX() >> 4, creator.getLocation().getBlockZ() >> 4, world, creator.getLocation());
		players = new HashMap<>();
		players.put(creator, Ranks.MAYOR);
		territory = new SettlementTerritoryManager(this);
		territory.add(new Territory(creator.getLocation().getWorld(), creator.getLocation().getBlockX() >> 4, creator.getLocation().getBlockZ() >> 4));
	}
	@Nullable
	public static Settlement loadFromFile(@Nullable String name){
		if (name == null){
			return null;
		}
		File settlementFile = new File(SettlementManager.getInstance().getSettlementDir(), name.toLowerCase() + ".settlement");
		return settlementFile.exists() ? new Settlement(settlementFile) : null;
	}
	private UUID uuidFromObject(Object obj){
		if (obj instanceof UUID){
			return  (UUID) obj;
		} else {
			String str = (String) obj;
			return UUID.fromString(str);
		}
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
				int x = (Integer) objects.get(1);
				int z = (Integer) objects.get(2);
				UUID world = UUID.fromString((String) objects.get(0));
				returnList.add(new Territory(world, x, z));
			} else if (o instanceof String){

			}
		}
		return returnList;
	}

	public NCPlayer getMayor(){
		for (Map.Entry<NCPlayer, Ranks> entry : players.entrySet()){
			if (!entry.getValue().equals(Ranks.MAYOR))
				continue;
			return entry.getKey();
		}
		return null;
	}

	
	public void addPlayer(NCPlayer p) {
		players.put(p, Ranks.CITIZEN);
	}
	
	public void removePlayer(UUID id) {
		players.remove(id);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@SuppressWarnings("static-access")
	public void setTownCenter(TownCenter townCenter) {
		this.townCenter = townCenter;
	}

	
	public boolean hasTownCenter() {
		if (townCenter == null) {
			return false;
		} else {
			return true;
		}
	}
	public float getExposurePercent(){
		ArrayList<Territory> surrounding = new ArrayList<>();
		for (Territory territory : getTerritory()){
			if (!getTerritory().contains(territory.getRelative(Direction.NORTH))){
				surrounding.add(territory.getRelative(Direction.NORTH));
			} else if (!getTerritory().contains(territory.getRelative(Direction.SOUTH))){
				surrounding.add(territory.getRelative(Direction.SOUTH));
			} else if (!getTerritory().contains(territory.getRelative(Direction.EAST))){
				surrounding.add(territory.getRelative(Direction.EAST));
			} else if (!getTerritory().contains(territory.getRelative(Direction.WEST))){
				surrounding.add(territory.getRelative(Direction.WEST));
			}
		}
		int territoriesNotOwnedByNation = 0;
		for (Territory territory : surrounding){
			if (NationManager.getInstance().getNationAt(territory) != null && NationManager.getInstance().getNationAt(territory).getName().equalsIgnoreCase(nation))
				continue;
			territoriesNotOwnedByNation++;
		}
		return ((float) territoriesNotOwnedByNation / (float) surrounding.size()) * 100f;
	}

	public void siege(Nation attacker){

	}

	public boolean isUnderSiege() {
		return underSiege;
	}

	public void saveToFile() {
		File settlementFile = SettlementManager.getInstance().getSettlementDir();
		if (!settlementFile.exists()) {
			try {
				settlementFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			try {

				PrintWriter writer = new PrintWriter(settlementFile);
				writer.println("name: " + getName());
				writer.println("nation: " + nation);
				writer.println("townCenter:");
				writer.println("  x: " + getTownCenter().getX());
				writer.println("  z: " + getTownCenter().getZ());
				writer.println("  world: " + getTownCenter().getWorld());
				writer.println("  teleportationPoint: [" + getTownCenter().getTeleportationPoint().getBlockX() + ", " + getTownCenter().getTeleportationPoint().getBlockY() + ", " + getTownCenter().getTeleportationPoint().getBlockZ() + "]");
				writer.println("territory:");
				if (!getTerritory().isEmpty()){
					for (Territory territory : getTerritory()) {
						if (territory == null){
							continue;
						}
						writer.println("- [" + territory.getWorld().toString() + "," + territory.getX() + "," + territory.getZ() + "]");
					}
				}

				writer.println("players:");
				for (NCPlayer player : getPlayers().keySet()){
					writer.write("  " + player.getPlayerID().toString() + ": " + getPlayers().get(player).name());
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public Nation getNation() {
		return NationManager.getInstance().getNationByName(nation);
	}
	public String getName() {
		return name;
	}
	
	public HashMap<NCPlayer, Ranks> getPlayers() {
		return players;
	}
	
	public TerritoryManager getTerritory() {
		return territory;
	}
	
	public TownCenter getTownCenter() {
		return townCenter;
	}

	public UUID getWorld(){
		return world;
	}

	public void setNation(String name) {
		this.nation = name;
	}

	private class SettlementNotFoundException extends RuntimeException{
		public SettlementNotFoundException(String s){
			super(s);
		}
	}
}
