package io.github.eirikh1996.nationcraft.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SubCommand {
    protected final Player sender;
    protected SubCommand(Player sender){
        this.sender = sender;
    }
    public abstract void execute();
}
