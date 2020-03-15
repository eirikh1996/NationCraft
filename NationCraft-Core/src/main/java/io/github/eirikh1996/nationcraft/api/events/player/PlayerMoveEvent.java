package io.github.eirikh1996.nationcraft.api.events.player;

import io.github.eirikh1996.nationcraft.api.events.Cancellable;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;

/**
 * Called when a player moves from one location to another
 */

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {

    private final NCLocation origin, destination;
    private boolean cancelled;

    /**
     * Constructs a new instance of PlayerMoveEvent
     * @param player the player that moves
     * @param origin the origin location of the moving player
     * @param destination the destination location of the moving player
     */
    public PlayerMoveEvent(NCPlayer player, NCLocation origin, NCLocation destination) {
        super(player);
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Gets the destination
     * @return the destination
     */

    public NCLocation getDestination() {
        return destination;
    }

    public NCLocation getOrigin() {
        return origin;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
