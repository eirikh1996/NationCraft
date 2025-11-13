package io.github.eirikh1996.nationcraft.sponge.objects;

import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;

public class NCSpongeConsole implements NCConsole {

    private final Server server;

    public NCSpongeConsole(Server server) {
        this.server = server;
    }

    @Override
    public void sendMessage(Component text) {
        server.sendMessage(text);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
