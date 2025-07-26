package io.github.eirikh1996.nationcraft.core.commands;

import net.kyori.adventure.text.Component;

public interface NCCommandSender {
    void sendMessage(Component text);
    void sendMessage(String message);
    void sendMessage(String[] messages);
    boolean hasPermission(String permission);
}
