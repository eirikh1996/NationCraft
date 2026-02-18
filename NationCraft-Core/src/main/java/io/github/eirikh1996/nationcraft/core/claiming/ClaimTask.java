package io.github.eirikh1996.nationcraft.core.claiming;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public abstract class ClaimTask implements Runnable {
    protected final @NotNull NCPlayer player;
    protected final @NotNull Shape shape;
    protected final int radius;

    public ClaimTask(@NotNull NCPlayer player, @NotNull Shape shape, int radius) {
        this.player = player;
        this.shape = shape;
        this.radius = radius;
    }

    public @NotNull NCPlayer getPlayer() {
        return player;
    }

    public @NotNull Shape getShape() {
        return shape;
    }

    public int getRadius() {
        return radius;
    }
}
