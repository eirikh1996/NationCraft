package io.github.eirikh1996.nationcraft.sponge.player;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.Optional;
import java.util.UUID;

public class NCSpongePlayer extends NCPlayer {
    public NCSpongePlayer(UUID playerID, String name) {
        super(playerID, name);
    }

    @Override
    public void sendMessage(@NotNull Component text) {
        final Optional<ServerPlayer> p = Sponge.server().player(playerID);
        if (p.isEmpty()) {
            return;
        }
        p.get().sendMessage(text);
    }

    @Override
    public void sendActionBar(@NotNull Component actionBar) {
        final Optional<ServerPlayer> p = Sponge.server().player(playerID);
        if (p.isEmpty()) {
            return;
        }
        p.get().sendActionBar(actionBar);
    }

    @Override
    public void teleport(NCLocation destination, String preTeleportMessage, String teleportMessage) {

    }


    @Override
    public NCLocation getLocation() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return Sponge.server().player(playerID).isPresent();
    }

    @Override
    public boolean charge(double fare) {
        return false;
    }

    @Override
    public void sendTitle(Title title) {
        final Optional<ServerPlayer> p = Sponge.server().player(playerID);
        if (p.isEmpty()) {
            return;
        }
        p.get().showTitle(title);
    }


    @Override
    public boolean hasPermission(String permission) {
        final Optional<ServerPlayer> sp = Sponge.server().player(playerID);
        return sp.map(player -> player.hasPermission(permission)).orElse(true);
    }
}
