package io.github.eirikh1996.nationcraft.core.commands;

public interface NCCommandSender {
    void sendMessage(String message);
    void sendMessage(String[] messages);
    boolean hasPermission(String permission);
}
