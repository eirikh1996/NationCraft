package io.github.eirikh1996.nationcraft.api.events.world;

import io.github.eirikh1996.nationcraft.api.events.Event;

import java.util.UUID;

public abstract class WorldEvent extends Event {
    private final UUID world;

    protected WorldEvent(UUID world) {
        this.world = world;
    }

    protected WorldEvent(UUID world, boolean async) {
        super(async);
        this.world = world;
    }
}
