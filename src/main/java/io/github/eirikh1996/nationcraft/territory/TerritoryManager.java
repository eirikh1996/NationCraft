package io.github.eirikh1996.nationcraft.territory;

import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.utils.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public interface TerritoryManager extends Iterable<Territory> {


    void claimCircularTerritory(Player player, int radius);
    void unclaimCircularTerritory(Player player, int radius);
    void claimSquareTerritory(Player player, int radius);
    void unclaimSquareTerritory(Player player, int radius);
    void claimLineTerritory(Player player, int distance);
    void unclaimLineTerritory(Player player, int distance);
    void unclaimAll(Player player);
    boolean addAll(Collection<? extends Territory> territories);
    int size();
    boolean contains(Territory territory);
    boolean isEmpty();
    Iterator<Territory> iterator() ;
}
