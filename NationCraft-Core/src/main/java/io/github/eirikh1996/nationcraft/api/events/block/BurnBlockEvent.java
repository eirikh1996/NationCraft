package io.github.eirikh1996.nationcraft.api.events.block;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.api.objects.NCBlock;

public class BurnBlockEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;
    public BurnBlockEvent(NCBlock block) {
        super(block);
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
