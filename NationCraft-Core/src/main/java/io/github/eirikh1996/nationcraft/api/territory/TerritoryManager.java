package io.github.eirikh1996.nationcraft.api.territory;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TerritoryManager implements Iterable<Territory> {
    private Set<Territory> territory = new HashSet<>();
    private ConcurrentMap<Nation, Collection<Territory>> nationTerritoryMap = new ConcurrentHashMap<>();
    private ConcurrentMap<Settlement, Collection<Territory>> settlementTerritoryMap = new ConcurrentHashMap<>();
    private NationCraftMain plugin;
    private static TerritoryManager INSTANCE;
    private TerritoryManager() {

    }
    public void addNationTerritory(Nation nation, Collection<Territory> newTerritory) {
        if (nationTerritoryMap.containsKey(nation)) {
            Collection<Territory> currentTerritory = nationTerritoryMap.get(nation);
            currentTerritory.addAll(newTerritory);
            nationTerritoryMap.put(nation, currentTerritory);
            return;
        }
        nationTerritoryMap.put(nation, newTerritory);
        saveToFile();
    }

    public void addSettlementTerritory(Settlement settlement, Collection<Territory> newTerritory) {
        if (settlementTerritoryMap.containsKey(settlement)) {

        }
        saveToFile();
    }

    public Collection<Territory> getTerritoryByNation(Nation nation) {
        return nationTerritoryMap.getOrDefault(nation, new HashSet<>());
    }

    public Collection<Territory> getTerritoryBySettlement(Settlement settlement) {
        return settlementTerritoryMap.getOrDefault(settlement, new HashSet<>());
    }
    private void loadFromFile() {
        File territoryFile = new File(plugin.getDataFolder(), "territory.yml");
        if (!territoryFile.exists())
            return;
        try {
            final InputStream inputStream = new FileInputStream(territoryFile);
            final Yaml yaml = new Yaml();
            final Map data = yaml.load(inputStream);
            final List<Map<String, Object>> list = (List) data.get("territory");
            for (Map<String, Object> entry : list) {
                Territory terr = Territory.deserialize(entry);
                territory.add(terr);
                Nation nation = NationManager.getInstance().getNationByUUID(UUID.fromString((String) entry.get("nation")));
                if (nation != null) {
                    if (nationTerritoryMap.containsKey(nation)) {
                        nationTerritoryMap.get(nation).add(terr);
                    } else {
                        Collection<Territory> territoryColl = new HashSet<>();
                        territoryColl.add(terr);
                        nationTerritoryMap.put(nation, territoryColl);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile() {
        File territoryFile = new File(plugin.getDataFolder(), "territory.yml");
        if (!territoryFile.exists()) {
            try {
                territoryFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            PrintWriter writer = new PrintWriter(territoryFile);
            writer.println("territory:");
            territory.forEach( t -> {
                boolean first = true;
                for (Map.Entry<String, Object> entry : t.serialize().entrySet()) {
                    writer.println(" " + (first ? "-" : " ") + entry.getKey() + ": " + entry.getValue());
                    first = false;
                }
            });
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    void claimCircularTerritory(NCPlayer player, int radius);
    void unclaimCircularTerritory(NCPlayer player, int radius);
    void claimSquareTerritory(NCPlayer player, int radius);
    void unclaimSquareTerritory(NCPlayer player, int radius);
    void claimLineTerritory(NCPlayer player, int distance);
    void unclaimLineTerritory(NCPlayer player, int distance);
    void claimSignleTerritory(NCPlayer player);
    void unclaimSignleTerritory(NCPlayer player);
    void unclaimAll(NCPlayer player);
    boolean add(Territory territory);
    boolean addAll(Collection<? extends Territory> territories);
    boolean remove(Territory territory);
    boolean removeAll(Collection<? extends Territory> territories);*/
    public int size() {
        return territory.size();
    }
    public boolean contains(Territory territory) {
        return this.territory.contains(territory);
    }
    public boolean isEmpty() {
        return territory.isEmpty();
    }
    @NotNull public Iterator<Territory> iterator() {
        return territory.iterator();
    }

    public static TerritoryManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TerritoryManager();
        }
        return INSTANCE;
    }

    public void initialize(NationCraftMain plugin) {
        this.plugin = plugin;
    }


}
