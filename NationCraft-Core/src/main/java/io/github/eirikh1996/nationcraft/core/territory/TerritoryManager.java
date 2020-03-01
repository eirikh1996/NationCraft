package io.github.eirikh1996.nationcraft.core.territory;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface TerritoryManager extends Iterable<Territory> {


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
    boolean removeAll(Collection<? extends Territory> territories);
    int size();
    boolean contains(Territory territory);
    boolean isEmpty();
    @NotNull Iterator<Territory> iterator() ;


}
