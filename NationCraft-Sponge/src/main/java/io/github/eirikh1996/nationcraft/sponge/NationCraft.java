package io.github.eirikh1996.nationcraft.sponge;

import com.google.inject.Inject;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.CommandRegistry;
import io.github.eirikh1996.nationcraft.core.commands.NCConsole;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;


import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Plugin(
        id = "nationcraft"
)
public class NationCraft implements NationCraftMain {


    @Inject
    private Logger logger;
    @Inject @DefaultConfig(sharedRoot = false) private Path defaultConfig;
    @Inject @ConfigDir(sharedRoot = false) private Path configDir;
    @Inject private PluginContainer container;
    @Inject private CommandRegistry commandRegistry;

    private NCConsole console;

    @Override
    public void logError(String errorMessage) {
        logger.severe(errorMessage);
    }

    @Override
    public void logWarning(String warningMessage) {
        logger.warning(warningMessage);
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
    public void onServerStart(StartingEngineEvent<Server> event) {
        Sponge.server()
    }

    @Listener
    public void onServerStarted(StartedEngineEvent<Server> event) {
        commandRegistry.registerDefaultCommands();
        for (Command cmd : commandRegistry) {
            org.spongepowered.api.command.Command.builder()
                    .executor(context -> {
                        context.cause()
                        return CommandResult.success();
                    })
        }
    }

    public void onCommandRegister(RegisterCommandEvent)
}
