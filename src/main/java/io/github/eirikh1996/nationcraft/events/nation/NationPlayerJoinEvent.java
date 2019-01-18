package io.github.eirikh1996.nationcraft.events.nation;

import io.github.eirikh1996.nationcraft.events.nation.NationEvent;
import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class NationPlayerJoinEvent extends NationEvent {
    private Player player;
    private static HandlerList HANDLERS = new HandlerList();
    public NationPlayerJoinEvent(Player p, Nation n){
        super(n);
        this.player = p;
    }

    public Player getPlayer(){
        return player;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
