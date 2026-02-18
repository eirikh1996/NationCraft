package io.github.eirikh1996.nationcraft.api.events.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.nation.Nation;

public abstract class NationPlayerEvent extends NationEvent{
    protected final NCPlayer player;
    protected NationPlayerEvent(Nation nation, NCPlayer player) {
        super(nation);
        this.player = player;
    }

    protected NationPlayerEvent(Nation nation, NCPlayer player, boolean isAsync) {
        super(nation, isAsync);
        this.player = player;
    }

    public NCPlayer getPlayer() {
        return player;
    }
}
