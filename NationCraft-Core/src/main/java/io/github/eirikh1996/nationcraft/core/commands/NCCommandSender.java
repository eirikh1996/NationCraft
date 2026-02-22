package io.github.eirikh1996.nationcraft.core.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * NationCraft representation of a Command sender
 */
public interface NCCommandSender {

    /**
     * Send a message to a command sender
     *
     * @param text Message to be sent
     */
    void sendMessage(Component text);


    /**
     * Send a message to a command sender
     *
     * @param message Message to be sent
     *
     * @deprecated Use sendMessage(Component text) instead
     */
    @Deprecated
    default void sendMessage(String message) {
        sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    /**
     * Send multiple messages to a command sender
     *
     * @param messages Messages to be sent
     *
     * @deprecated Use sendMessage(Component text) instead
     */
    @Deprecated
    default void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
        }
    }
    boolean hasPermission(String permission);
}
