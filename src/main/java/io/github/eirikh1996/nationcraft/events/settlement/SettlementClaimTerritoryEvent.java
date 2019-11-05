package io.github.eirikh1996.nationcraft.events.settlement;

import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SettlementClaimTerritoryEvent extends SettlementEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final ArrayList<Territory> newClaims;
    private final ArrayList<Territory> originalClaims;
    public SettlementClaimTerritoryEvent(Settlement settlement, ArrayList<Territory> newClaims, ArrayList<Territory> originalClaims){
        super(settlement);

        this.newClaims = newClaims;
        this.originalClaims = originalClaims;
    }
    public SettlementClaimTerritoryEvent(Settlement settlement, boolean isAsync, ArrayList<Territory> newClaims, ArrayList<Territory> originalClaims) {
        super(settlement, isAsync);
        this.newClaims = newClaims;
        this.originalClaims = originalClaims;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public ArrayList<Territory> getNewClaims() {
        return newClaims;
    }

    public ArrayList<Territory> getOriginalClaims() {
        return originalClaims;
    }
}
