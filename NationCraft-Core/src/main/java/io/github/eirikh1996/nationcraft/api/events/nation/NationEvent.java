package io.github.eirikh1996.nationcraft.api.events.nation;

import io.github.eirikh1996.nationcraft.api.events.Event;
import io.github.eirikh1996.nationcraft.api.nation.Nation;

public abstract class NationEvent extends Event {
    private final Nation nation;

    protected NationEvent(Nation nation) {
        this.nation = nation;
    }

    public Nation getNation() {
        return nation;
    }
}
