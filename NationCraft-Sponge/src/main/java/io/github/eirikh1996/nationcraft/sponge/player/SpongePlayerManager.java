package io.github.eirikh1996.nationcraft.sponge.player;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import org.spongepowered.api.entity.living.player.Player;

import java.util.UUID;

public class SpongePlayerManager extends PlayerManager {
    @Override
    public void addPlayer(UUID id, String name) {

    }

    @Override
    public <P> NCPlayer getPlayer(P player) {
        if (!(player instanceof Player))
            throw new IllegalArgumentException();
        return players.get(((Player) player).getUniqueId());
    }
}
