package io.github.eirikh1996.nationcraft.core.settlement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

public class SettlementManager implements Iterable<Settlement>{
	private static SettlementManager instance;
	private static NationCraftMain main;
	private final File settlementsDir;
	private final Map<UUID, Settlement> settlements = new ConcurrentHashMap<>();
	//This is a singleton object, so don't instantiate it
	private SettlementManager(){
		settlementsDir = new File(main.getDataFolder(), "settlements");
		if (!settlementsDir.exists())
			settlementsDir.mkdirs();
		loadSettlements();
	}

	public static void initialize(NationCraftMain main) {
		SettlementManager.main = main;
		instance = new SettlementManager();
	}

	public Settlement getSettlementsByUUID(UUID uuid) {
		if (uuid == null)
			return null;
		return settlements.get(uuid);
	}

	public Settlement getSettlementsByName(String name) {
		for (Settlement settlement : settlements.values()) {
			if (!settlement.getName().equals(name))
				continue;
			return settlement;
		}
		return null;
	}

	public Set<Settlement> getSettlementsByNation(Nation nation) {
		final Set<Settlement> nationSettlements = new HashSet<>();
		for (Settlement settlement : settlements.values()) {
			if (settlement.getNation() == null || !settlement.getNation().equals(nation))
				continue;
			nationSettlements.add(settlement);
		}
		return nationSettlements;
	}

	public void loadSettlements() {
		final File[] settlementFiles = settlementsDir.listFiles();
		for (File settlementFile : settlementFiles) {
			if (settlementFile == null || !settlementFile.getName().endsWith(".settlement")) {
				continue;
			}
			try {
				Yaml yaml = new Yaml();
				InputStream input = new FileInputStream(settlementFile);
				final Map data = yaml.load(input);
				input.close();
				String name = (String) data.get("name");
				UUID uuid = UUID.fromString((String) data.get("uuid"));
				if (settlementFile.getName().startsWith(name.toLowerCase())) {
					settlementFile.renameTo(new File(settlementsDir, uuid + ".settlement"));
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			final Settlement settlement = new Settlement(settlementFile);
			settlements.put(settlement.getUuid(), settlement);
		}
		main.logInfo("Loaded " + settlements.size() + " settlement files");
	}

	public static SettlementManager getInstance() {
		return instance;
	}

	@Nullable
	public Settlement getSettlementByName(String name){
		for (Settlement settlement : settlements.values()) {
			if (!settlement.getName().equals(name))
				continue;
			return settlement;
		}
		return null;
	}
	public Settlement getSettlementByPlayer(NCPlayer player){
		for (Settlement settlement : settlements.values()) {
			if (!settlement.getPlayers().containsKey(player))
				continue;
			return settlement;
		}
		return null;
	}

	public Settlement getSettlementByPlayer(UUID playerID){
		return PlayerManager.getInstance().getPlayer(playerID).getSettlement();
	}

	public Settlement getSettlementAt(NCLocation loc){
		return getSettlementAt(new Territory(loc.getWorld(), loc.getChunkX(), loc.getChunkZ()));
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

	public Settlement getSettlementByUUID(UUID uuid) {
		return settlements.get(uuid);
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

	@Deprecated
	public File getSettlementDir() {
		return getSettlementsDir();
	}

	@NotNull
	@Override
	public Iterator<Settlement> iterator() {
		return Collections.unmodifiableCollection(getAllSettlements()).iterator();
	}

    public File getSettlementsDir() {
		if (!settlementsDir.exists())
			settlementsDir.mkdirs();
        return settlementsDir;
    }

	public void addSettlement(Settlement settlement) {
		settlements.put(settlement.getUuid(), settlement);
	}
}
