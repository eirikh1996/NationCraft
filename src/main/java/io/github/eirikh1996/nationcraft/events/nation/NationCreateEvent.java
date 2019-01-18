package io.github.eirikh1996.nationcraft.events.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class NationCreateEvent extends NationEvent {
    private final Player player;
    private static HandlerList handlers = new HandlerList();
    public NationCreateEvent(Nation nation, Player player){
        super(nation);
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public Player getPlayer(){
        return player;
    }
}
