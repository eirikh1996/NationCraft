package io.github.eirikh1996.nationcraft.territory;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface TerritoryManager extends Iterable<Territory> {


    void claimCircularTerritory(Player player, int radius);
    void unclaimCircularTerritory(Player player, int radius);
    void claimSquareTerritory(Player player, int radius);
    void unclaimSquareTerritory(Player player, int radius);
    void claimLineTerritory(Player player, int distance);
    void unclaimLineTerritory(Player player, int distance);
    void claimSignleTerritory(Player player);
    void unclaimSignleTerritory(Player player);
    void unclaimAll(Player player);
    boolean add(Territory territory);
    boolean addAll(Collection<? extends Territory> territories);
    boolean remove(Territory territory);
    boolean removeAll(Collection<? extends Territory> territories);
    int size();
    boolean contains(Territory territory);
    boolean isEmpty();
    @NotNull Iterator<Territory> iterator() ;


}
