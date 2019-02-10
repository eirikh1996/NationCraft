package io.github.eirikh1996.nationcraft.events.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class NationClaimTerritoryEvent extends NationEvent {
    private static HandlerList handlerList = new HandlerList();
    private Set<Chunk> claimedTerritory;

    public NationClaimTerritoryEvent(Nation n, Set<Chunk> claimedTerritory){
        super(n);
        this.claimedTerritory = claimedTerritory;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    public Set<Chunk> getClaimedTerritory() {
        return claimedTerritory;
    }


}
