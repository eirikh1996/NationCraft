package io.github.eirikh1996.nationcraft.events.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.Set;

public class NationClaimTerritoryEvent extends NationEvent {
    private static HandlerList handlerList = new HandlerList();
    private final ArrayList<Territory> originalTerritory;
    private final ArrayList<Territory> newTerritory;

    public NationClaimTerritoryEvent(Nation n, ArrayList<Territory> originalTerritory, ArrayList<Territory> newTerritory){
        super(n);
        this.originalTerritory = originalTerritory;
        this.newTerritory = newTerritory;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    public ArrayList<Territory> getNewTerritory() {
        return newTerritory;
    }

    public ArrayList<Territory> getOriginalTerritory() {
        return originalTerritory;
    }
}
