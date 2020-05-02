package io.github.eirikh1996.nationcraft.sponge.player;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.Optional;
import java.util.UUID;

public class NCSpongePlayer extends NCPlayer {
    public NCSpongePlayer(UUID playerID, String name) {
        super(playerID, name);
    }

    @Override
    public void sendMessage(@NotNull ChatText text) {
        final Optional<Player> p = Sponge.getServer().getPlayer(playerID);
        if (!p.isPresent()) {
            return;
        }
        p.get().sendMessage(TextSerializers.JSON.deserialize(text.json()));
    }

    @Override
    public void sendActionBar(@NotNull String text) {
        final Optional<Player> p = Sponge.getServer().getPlayer(playerID);
        if (!p.isPresent()) {
            return;
        }
        final Title spongetitle = Title.builder()
                .actionBar(TextSerializers.LEGACY_FORMATTING_CODE.deserialize(text))
                .build();
        p.get().sendTitle(spongetitle);
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
        return Sponge.getServer().getPlayer(playerID).isPresent();
    }

    @Override
    public boolean charge(double fare) {
        return false;
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        final Optional<Player> p = Sponge.getServer().getPlayer(playerID);
        if (!p.isPresent()) {
            return;
        }
        final Title spongetitle = Title.builder()
                .title(TextSerializers.LEGACY_FORMATTING_CODE.deserialize(title))
                .subtitle(TextSerializers.LEGACY_FORMATTING_CODE.deserialize(subtitle))
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut)
                .build();
        p.get().sendTitle(spongetitle);
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
