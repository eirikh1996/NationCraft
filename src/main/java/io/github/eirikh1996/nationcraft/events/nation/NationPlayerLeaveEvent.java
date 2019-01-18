package io.github.eirikh1996.nationcraft.events.nation;

import io.github.eirikh1996.nationcraft.events.nation.NationEvent;
import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class NationPlayerLeaveEvent extends NationEvent {
    private Player player;
    private static HandlerList handlers = new HandlerList();
    public NationPlayerLeaveEvent(Nation nation, Player player){
        super(nation);
        this.player = player;
    }
    public static HandlerList getHandlerList(){
        return handlers;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
