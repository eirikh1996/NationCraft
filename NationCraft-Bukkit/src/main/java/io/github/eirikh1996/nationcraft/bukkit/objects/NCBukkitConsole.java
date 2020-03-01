package io.github.eirikh1996.nationcraft.bukkit.objects;

import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import org.bukkit.command.ConsoleCommandSender;

public class NCBukkitConsole implements NCConsole {

    private final ConsoleCommandSender console;

    public NCBukkitConsole(ConsoleCommandSender console) {
        this.console = console;
    }
    @Override
    public void sendMessage(String message) {
        console.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        console.sendMessage(messages);
    }

    @Override
    public boolean hasPermission(String permission) {
        return console.hasPermission(permission);
    }
}
