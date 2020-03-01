package io.github.eirikh1996.nationcraft.api.events.block;

import io.github.eirikh1996.nationcraft.api.events.Event;
import io.github.eirikh1996.nationcraft.api.objects.NCBlock;

public abstract class BlockEvent extends Event {
    private final NCBlock block;

    protected BlockEvent(NCBlock block) {
        this.block = block;
    }

    public NCBlock getBlock() {
        return block;
    }
}
