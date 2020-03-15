package io.github.eirikh1996.nationcraft.api.settlement;

import java.io.File;
import java.util.*;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.territory.Territory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettlementManager implements Iterable<Settlement>{
	private static SettlementManager instance;
	private static NationCraftMain main;
	//This is a singleton object, so don't instantiate it
	private SettlementManager(){
	}

	public static void initialize(NationCraftMain main) {
		instance = new SettlementManager();
		SettlementManager.main = main;
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
	public Settlement getSettlementByPlayer(NCPlayer player){
		Nation n = player.getNation();
		if (n != null){
			for (Settlement s : n.getSettlements()){
				if (s == null || !s.getPlayers().containsKey(player)){
					continue;
				}
				return s;
			}
			if (n.getCapital() != null && n.getCapital().getPlayers().containsKey(player)){
				return n.getCapital();
			}
		}
		return null;
	}

	public Settlement getSettlementByPlayer(UUID playerID){
		return PlayerManager.getInstance().getPlayer(playerID).getSettlement();
	}

	public Settlement getSettlementAt(NCLocation loc){
		return getSettlementAt(new Territory(loc.getWorld(), loc.getBlockX() >> 4, loc.getBlockZ() >> 4));
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

	public Set<Settlement> getAllSettlements(){
		HashSet<Settlement> returnList = new HashSet<>();
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

	public File getSettlementDir() {
		final File dir = new File(main.getDataFolder(), "settlements");
		if (!dir.exists())
			dir.mkdirs();
		return dir;
	}

	@NotNull
	@Override
	public Iterator<Settlement> iterator() {
		return Collections.unmodifiableCollection(getAllSettlements()).iterator();
	}
}
