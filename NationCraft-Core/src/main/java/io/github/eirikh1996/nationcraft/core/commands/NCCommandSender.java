package io.github.eirikh1996.nationcraft.core.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public interface NCCommandSender {
    void sendMessage(Component text);

    @Deprecated
    default void sendMessage(String message) {
        sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Deprecated
    default void sendMessage(String[] messages) {
        TextComponent text = Component.empty();
        for (String message : messages) {
            text = text.append(LegacyComponentSerializer.legacySection().deserialize(message));
        }
        sendMessage(text);
    }
    boolean hasPermission(String permission);
}
