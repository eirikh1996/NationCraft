package io.github.eirikh1996.nationcraft.api;

import io.github.eirikh1996.nationcraft.core.commands.NCBlockSender;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;

import java.io.File;

public interface NationCraftMain {
    void logError(String errorMessage);
    void logWarning(String warningMessage);
    void logInfo(String infoMessage);
    void readConfig();
    void broadcast(String message);
    NCConsole getConsole();
    NationCraftAPI getAPI();
    File getDataFolder();
}
