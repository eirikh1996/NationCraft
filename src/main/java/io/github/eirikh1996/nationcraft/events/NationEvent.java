package io.github.eirikh1996.nationcraft.events;

import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class NationEvent extends Event implements Cancellable {
    private final Nation nation;
    private boolean isCancelled = false;

    public NationEvent(Nation nation){
        this.nation = nation;
    }
    public NationEvent(Nation nation, boolean isAsync){
        super(isAsync);
        this.nation = nation;
    }

    public Nation getNation(){
        return nation;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }
}
