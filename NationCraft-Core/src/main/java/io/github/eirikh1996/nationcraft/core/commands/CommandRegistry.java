package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.events.command.CommandProcessEvent;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

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

    public Map<String, Command> getRegisteredCommands() {
        return registeredCommands;
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
        final Command cmd = registeredCommands.get(commandName);
        final String cmdPerm = cmd.getPermission();
        if (!cmdPerm.isEmpty() && !sender.hasPermission(cmdPerm)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(NO_PERMISSION));
            return;
        }
        //Call event
        final CommandProcessEvent event = new CommandProcessEvent(sender, cmd, args);
        NationCraftAPI.getInstance().callEvent(event);
        if (event.isCancelled())
            return;
        if (!cmd.validateCommandSender(sender)) {
            //Validate command sender as some commands are only supposed
            //to be executed by certain types of senders, such as players
            return;
        }

        if (args.length == 0) { //Execute parent command if no arguments are parsed
            cmd.execute(sender);
            return;
        }
        Command command = cmd;
        for (int i = 0; i < args.length ; i++) {
            Optional<Command> child = command.getChild(args[i]);
            if (child.isEmpty() && command.getRegisteredParameters() == 0) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Messages.ERROR).append(Component.text("Invalid subcommand: " + args[i])));
                return;
            }

            if (command.getRegisteredParameters() > 0) {
                for (int j = 0; j < command.getRegisteredParameters(); j++) {
                    int index = j + i + 1;
                    String input;
                    if (index < args.length) {
                        input = args[index];
                    } else {
                        input = "";
                    }
                    command.getParameterByIndex(j).parse(sender, input);
                }
            }
            if (!command.hasChildren()) {
                command.execute(sender);
                break;
            }
            command = child.get();
        } //n claim(0) square(1) 100(2) nation(3)
    }

    @NotNull
    @Override
    public Iterator<Command> iterator() {
        return Collections.unmodifiableCollection(registeredCommands.values()).iterator();
    }
}
