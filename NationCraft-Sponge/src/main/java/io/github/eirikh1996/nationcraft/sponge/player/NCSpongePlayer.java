package io.github.eirikh1996.nationcraft.sponge.player;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

public class NCSpongePlayer extends NCPlayer {
    public NCSpongePlayer(UUID playerID, String name) {
        super(playerID, name);
    }



    @Override
    public NCLocation getLocation() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return Sponge.getServer().getPlayer(playerID).isPresent();
    }

    @Override
    public boolean charge(double fare) {
        return false;
    }

    @Override
    public void sendMessage(String message) {
        final Optional<Player> sp = Sponge.getServer().getPlayer(playerID);
        if (!sp.isPresent())
            return;
        sp.get().sendMessage(Text.of(message));
    }

    @Override
    public void sendMessage(String[] messages) {
        final Optional<Player> sp = Sponge.getServer().getPlayer(playerID);
        if (!sp.isPresent())
            return;
        for (String msg : messages) {
            sp.get().sendMessage(Text.of(msg));
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        final Optional<Player> sp = Sponge.getServer().getPlayer(playerID);
        return sp.map(player -> player.hasPermission(permission)).orElse(true);
    }
}
