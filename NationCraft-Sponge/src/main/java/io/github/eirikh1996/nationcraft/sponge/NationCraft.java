package io.github.eirikh1996.nationcraft.sponge;

import com.google.inject.Inject;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Plugin(
        id = "nationcraft",
        name = "NationCraft",
        version = "1.0"
)
public class NationCraft implements NationCraftMain {


    @Inject private Logger logger;
    @Inject @DefaultConfig(sharedRoot = false) private Path defaultConfig;
    @Inject @ConfigDir(sharedRoot = false) private Path configDir;
    @Inject private PluginContainer container;

    @Override
    public void logError(String errorMessage) {
        logger.error(errorMessage);
    }

    @Override
    public void logWarning(String warningMessage) {
        logger.warn(warningMessage);
    }

    @Override
    public void logInfo(String infoMessage) {
        logger.info(infoMessage);
    }

    @Override
    public void readConfig() {

    }

    @Override
    public void broadcast(String message) {

    }

    @Override
    public NCConsole getConsole() {
        return null;
    }

    @Override
    public NationCraftAPI getAPI() {
        return null;
    }

    @Override
    public File getDataFolder() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public List<String> getAuthors() {
        return null;
    }

    @Listener
    public void onServerStart(GameStartingServerEvent event) {

    }
}
