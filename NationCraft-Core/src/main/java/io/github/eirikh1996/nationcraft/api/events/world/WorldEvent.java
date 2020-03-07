package io.github.eirikh1996.nationcraft.api.events.world;

import io.github.eirikh1996.nationcraft.api.events.Event;

public abstract class WorldEvent extends Event {
    private final String world;

    protected WorldEvent(String world) {
        this.world = world;
    }

    protected WorldEvent(String world, boolean async) {
        super(async);
        this.world = world;
    }
}
