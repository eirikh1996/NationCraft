package io.github.eirikh1996.nationcraft.bukkit.objects;

import io.github.eirikh1996.nationcraft.core.commands.NCBlockSender;
import org.bukkit.command.BlockCommandSender;

public class NCBukkitBlockSender implements NCBlockSender {
    private final BlockCommandSender sender;


    public NCBukkitBlockSender(BlockCommandSender sender) {
        this.sender = sender;
    }
    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        sender.sendMessage(messages);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
}
