package io.github.eirikh1996.nationcraft.api;

import io.github.eirikh1996.nationcraft.core.commands.NCBlockSender;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import io.github.eirikh1996.nationcraft.core.listener.BlockListener;
import io.github.eirikh1996.nationcraft.core.listener.PlayerListener;

import java.io.File;
import java.util.List;

public interface NationCraftMain {
    void logError(String errorMessage);
    void logWarning(String warningMessage);
    void logInfo(String infoMessage);
    void readConfig();
    void broadcast(String message);
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
