package io.github.eirikh1996.nationcraft.events.settlement;

import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SettlementUnclaimTerritoryEvent extends SettlementEvent {
    private static final HandlerList handlerList = new HandlerList();
    private final ArrayList<Territory> removedClaims;
    private final ArrayList<Territory> originalClaims;

    public SettlementUnclaimTerritoryEvent(Settlement settlement, ArrayList<Territory> removedClaims, ArrayList<Territory> originalClaims) {
        super(settlement);
        this.removedClaims = removedClaims;
        this.originalClaims = originalClaims;
    }

    public SettlementUnclaimTerritoryEvent(Settlement settlement, boolean isAsync, ArrayList<Territory> removedClaims, ArrayList<Territory> originalClaims) {
        super(settlement, isAsync);
        this.removedClaims = removedClaims;
        this.originalClaims = originalClaims;
    }

    public ArrayList<Territory> getOriginalClaims() {
        return originalClaims;
    }

    public ArrayList<Territory> getRemovedClaims() {
        return removedClaims;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
