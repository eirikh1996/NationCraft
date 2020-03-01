package io.github.eirikh1996.nationcraft.api.events.block;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.api.objects.NCBlock;

public class LiquidFlowEvent extends BlockEvent implements Cancellable{
    private final NCBlock destination;
    private boolean cancelled;
    public LiquidFlowEvent(NCBlock block, NCBlock destination) {
        super(block);
        this.destination = destination;
    }

    public NCBlock getDestination() {
        return destination;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
