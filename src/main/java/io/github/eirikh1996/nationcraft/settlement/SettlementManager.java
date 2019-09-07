package io.github.eirikh1996.nationcraft.settlement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettlementManager {
	private static SettlementManager instance;
	//This is a singleton object, so don't instantiate it
	private SettlementManager(){
	}
	public static void initialize(){
		instance = new SettlementManager();
	}

	public static SettlementManager getInstance() {
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

	public Settlement getSettlementByPlayer(Player player){
		Nation n = NationManager.getInstance().getNationByPlayer(player);
		if (n != null){
			for (Settlement s : n.getSettlements()){
				if (s == null || !s.getPlayers().containsKey(player.getUniqueId())){
					continue;
				}
				return s;
			}
			if (n.getCapital() != null && n.getCapital().getPlayers().containsKey(player.getUniqueId())){
				return n.getCapital();
			}
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
		return returnList;
	}
}
