package io.github.eirikh1996.nationcraft.settlement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettlementManager {
	private static SettlementManager instance;
	//This is a singleton object, so don't instantiate it
	private SettlementManager(){
	}


	public static SettlementManager getInstance() {
		if (instance == null){
			instance = new SettlementManager();
		}
		return instance;
	}

	@Nullable
	public Settlement getSettlementByName(String name){
		for (Nation n : NationManager.getInstance()){
			if (!n.getSettlements().isEmpty()){
				for (Settlement ret : n.getSettlements()){
					if (ret != null && ret.getName().equalsIgnoreCase(name)){
						return ret;
					}
				}
			}
			if (n.getCapital() != null && n.getCapital().getName().equalsIgnoreCase(name)){
				return n.getCapital();
			}
		}
		return null;
	}

	public Settlement getSettlementByPlayer(UUID playerID){
		Nation n = NationManager.getInstance().getNationByPlayer(playerID);
		if (n != null){
			for (Settlement s : n.getSettlements()){
				if (s == null || !s.getPlayers().containsKey(playerID)){
					continue;
				}
				return s;
			}
			if (n.getCapital() != null && n.getCapital().getPlayers().containsKey(playerID)){
				return n.getCapital();
			}
		}
		return null;
	}

	public Settlement getSettlementAt(Location loc){
		return getSettlementAt(loc.getChunk());
	}

	public Settlement getSettlementAt(Chunk chunk){
		return getSettlementAt(Territory.fromChunk(chunk));
	}

	public Settlement getSettlementAt(Territory territory){
		for (Settlement s : getAllSettlements()){
			if (!s.getTerritory().contains(territory)){
				continue;
			}
			return s;
		}
		return null;
	}

	public ArrayList<Settlement> getAllSettlements(){
		ArrayList<Settlement> returnList = new ArrayList<>();
		for (Nation n : NationManager.getInstance()){
			if (!n.getSettlements().isEmpty())
				returnList.addAll(n.getSettlements());
			if (n.getCapital() != null)
				returnList.add(n.getCapital());
		}
		Iterator<Settlement> iterator = returnList.iterator();
		while (iterator.hasNext()){
			if (iterator.next() != null){
				continue;
			}
			iterator.remove();
		}
		return returnList;
	}
}
