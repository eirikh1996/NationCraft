package io.github.eirikh1996.nationcraft.api.territory;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TerritoryManager implements Iterable<Territory> {
    private ConcurrentMap<Nation, Collection<Territory>> nationTerritoryMap = new ConcurrentHashMap<>();
    private ConcurrentMap<Settlement, Collection<Territory>> settlementTerritoryMap = new ConcurrentHashMap<>();
    private NationCraftMain plugin;
    private static TerritoryManager INSTANCE;
    private static Yaml yaml;
    private TerritoryManager() {

    }
    public void addNationTerritory(Nation nation, Territory... newTerritory) {
        addNationTerritory(nation, newTerritory);
    }

    public void removeNationTerritory(Nation nation, Collection<Territory> oldTerritory) {
        if (!nationTerritoryMap.containsKey(nation)) {
            return;
        }
        Collection<Territory> currentTerritory = nationTerritoryMap.get(nation);
        currentTerritory.removeAll(oldTerritory);
        if (currentTerritory.isEmpty()) {
            nationTerritoryMap.remove(nation);
        } else {
            nationTerritoryMap.put(nation, currentTerritory);
        }
        saveToFile();
    }

    public void addNationTerritory(Nation nation, Collection<Territory> newTerritory) {
        if (nationTerritoryMap.containsKey(nation)) {
            Collection<Territory> currentTerritory = nationTerritoryMap.get(nation);
            currentTerritory.addAll(newTerritory);
            nationTerritoryMap.put(nation, currentTerritory);
        } else {
            nationTerritoryMap.put(nation, newTerritory);
        }
        saveToFile();
    }

    public void addSettlementTerritory(Settlement settlement, Collection<Territory> newTerritory) {
        if (settlementTerritoryMap.containsKey(settlement)) {
            Collection<Territory> currentTerritory = settlementTerritoryMap.get(settlement);
            currentTerritory.addAll(newTerritory);
            settlementTerritoryMap.put(settlement, currentTerritory);
        } else {
            settlementTerritoryMap.put(settlement, newTerritory);
        }
        saveToFile();
    }

    public void removeSettlementTerritory(Settlement settlement, Collection<Territory> oldTerritory) {
        if (!settlementTerritoryMap.containsKey(settlement)) {
            return;
        }
        Collection<Territory> currentTerritory = settlementTerritoryMap.get(settlement);
        currentTerritory.removeAll(oldTerritory);
        if (currentTerritory.isEmpty()) {
            settlementTerritoryMap.remove(settlement);
        } else {
            settlementTerritoryMap.put(settlement, currentTerritory);
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
            final Map data = yaml.load(inputStream);
            final List<Map<String, Object>> list = (List) data.get("territory");
            if (list == null) {
                return;
            }
            for (Map<String, Object> entry : list) {
                Territory terr = Territory.deserialize(entry);
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
                String settlementId = (String) entry.get("settlement");
                if (settlementId == null) {
                    continue;
                }
                Settlement settlement = SettlementManager.getInstance().getSettlementByUUID(UUID.fromString(settlementId));
                if (settlement == null) {
                    continue;
                }
                if (settlementTerritoryMap.containsKey(settlement)) {
                    settlementTerritoryMap.get(settlement).add(terr);
                } else {
                    Collection<Territory> terriltoryColl = new HashSet<>();
                    terriltoryColl.add(terr);
                    settlementTerritoryMap.put(settlement, terriltoryColl);
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
            getClaimedTerritory().forEach( t -> {
                boolean first = true;
                for (Map.Entry<String, Object> entry : t.serialize().entrySet()) {
                    writer.println((first ? "- " : "  ") + entry.getKey() + ": " + entry.getValue());
                    first = false;
                }
            });
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Territory> getClaimedTerritory() {
        HashSet<Territory> claimedTerritory = new HashSet<>();
        nationTerritoryMap.forEach((n, t) -> claimedTerritory.addAll(t));
        settlementTerritoryMap.forEach((s, t) -> claimedTerritory.addAll(t));
        return claimedTerritory;
    }

    public int size() {
        return getClaimedTerritory().size();
    }
    public boolean contains(Territory territory) {
        return getClaimedTerritory().contains(territory);
    }
    public boolean isEmpty() {
        return getClaimedTerritory().isEmpty();
    }
    @NotNull public Iterator<Territory> iterator() {
        return Collections.unmodifiableSet(getClaimedTerritory()).iterator();
    }

    public static TerritoryManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TerritoryManager();
        }
        return INSTANCE;
    }

    public void initialize(NationCraftMain plugin) {
        this.plugin = plugin;
        final LoaderOptions options = new LoaderOptions();
        options.setCodePointLimit(Integer.MAX_VALUE);
        yaml = new Yaml(options);
        loadFromFile();
    }


}
