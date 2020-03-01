package io.github.eirikh1996.nationcraft.api.events;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
