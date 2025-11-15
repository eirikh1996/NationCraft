package io.github.eirikh1996.nationcraft.api;

import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import io.github.eirikh1996.nationcraft.core.listener.BlockListener;
import io.github.eirikh1996.nationcraft.core.listener.PlayerListener;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.io.File;
import java.util.List;

public interface NationCraftMain {
    void logError(String errorMessage);
    void logWarning(String warningMessage);
    void logInfo(String infoMessage);
    void readConfig();
    @Deprecated
    default void broadcast(String message) {
        broadcast(LegacyComponentSerializer.legacySection().deserialize(message));
    }
    void broadcast(TextComponent message);
    NCConsole getConsole();
    NationCraftAPI getAPI();
    File getDataFolder();
    default void registerCoreListeners() {
        final NationCraftAPI api = NationCraftAPI.getInstance();
        api.registerEvent(new BlockListener());
        api.registerEvent(new PlayerListener());
    }
    String getVersion();
    List<String> getAuthors();
}
