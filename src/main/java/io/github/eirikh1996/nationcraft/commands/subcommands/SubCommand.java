package io.github.eirikh1996.nationcraft.commands.subcommands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    private final CommandSender sender;
    public SubCommand(CommandSender sender){
        this.sender = sender;
    }
    public abstract void execute();
}
