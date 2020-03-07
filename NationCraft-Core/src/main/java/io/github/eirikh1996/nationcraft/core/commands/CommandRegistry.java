package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.core.commands.subcommands.map.HeightSubCommand;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandRegistry implements Iterable<Command> {

    private final Map<String, Command> registeredCommands = new HashMap<>();

    public boolean isRegistered(String command) {
        return registeredCommands.containsKey(command);
    }

    public void register(Command cmd) {
        registeredCommands.put(cmd.getName(), cmd);
        if (cmd.getAliases().isEmpty()) {
            return;
        }
        for (String alias : cmd.getAliases()) {
            registeredCommands.put(alias, cmd);
        }
    }

    public void registerDefaultCommands() {
        register(new ChatModeCommand());
        register(new NationCraftCommand());
        register(new NationCommand());
        register(new MapCommand());
        register(new SettlementCommand());
        register(new PlayerCommand());
    }

    public Command getCommand(String name) {
        if (!registeredCommands.containsKey(name)) {
            throw new IllegalArgumentException("Command " + name + " is not registered");
        }
        return registeredCommands.get(name);
    }

    public void runCommand(String commandName, NCCommandSender sender, String[] args) {
        if (!isRegistered(commandName)) {
            throw new IllegalArgumentException("Command " + commandName + " is not registered");
        }
        registeredCommands.get(commandName).execute(sender, args);
    }

    @NotNull
    @Override
    public Iterator<Command> iterator() {
        return Collections.unmodifiableCollection(registeredCommands.values()).iterator();
    }
}
