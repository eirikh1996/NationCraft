package io.github.eirikh1996.nationcraft.api.events.world;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.events.Cancellable;

public class TerrainEditEvent extends WorldEvent implements Cancellable {
    private final Action action;
    private boolean cancelled = false;
    public TerrainEditEvent(NCLocation location, Action action) {
        super(location.getWorld());
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public enum  Action {
        BREAK_BLOCK, PLACE_BLOCK, CHANGE_ITEM_FRAME,
    }
}
