package io.github.eirikh1996.nationcraft.events;


import io.github.eirikh1996.nationcraft.settlement.Settlement;
import org.bukkit.event.Event;

public abstract class SettlementEvent extends Event {
    private final Settlement settlement;
    public SettlementEvent(Settlement settlement, boolean isAsync){
        super(isAsync);
        this.settlement = settlement;
    }

    public Settlement getSettlement() {
        return settlement;
    }
}
