package io.github.eirikh1996.nationcraft.events.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class NationPlayerInviteEvent extends NationEvent {
    private static HandlerList handlers = new HandlerList();
    private UUID invitedID;
    public NationPlayerInviteEvent(Nation n, UUID invitedID){
        super(n);
        this.invitedID = invitedID;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public UUID getInvitedID(){
        return invitedID;
    }
    public static HandlerList getHandlerList(){
        return handlers;
    }
}
