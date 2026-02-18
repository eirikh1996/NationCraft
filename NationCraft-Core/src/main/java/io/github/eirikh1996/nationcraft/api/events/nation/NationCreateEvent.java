package io.github.eirikh1996.nationcraft.api.events.nation;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;

public class NationCreateEvent extends NationPlayerEvent implements Cancellable {

    private boolean cancelled;

    public NationCreateEvent(Nation nation, NCPlayer player) {
        super(nation, player);
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
