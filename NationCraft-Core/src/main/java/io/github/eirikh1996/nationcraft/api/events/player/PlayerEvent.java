package io.github.eirikh1996.nationcraft.api.events.player;

import io.github.eirikh1996.nationcraft.api.events.Event;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;

public abstract class PlayerEvent extends Event {
    private final NCPlayer player;

    protected PlayerEvent(NCPlayer player) {
        this.player = player;
    }

    protected PlayerEvent(NCPlayer player, boolean async) {
        super(async);
        this.player = player;
    }

    public NCPlayer getPlayer() {
        return player;
    }
}
