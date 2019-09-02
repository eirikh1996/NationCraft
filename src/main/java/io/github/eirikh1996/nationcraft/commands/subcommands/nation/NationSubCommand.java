package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.commands.subcommands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class NationSubCommand extends SubCommand {
    protected final Player sender;
    public NationSubCommand(Player sender) {
        super(sender);
        this.sender = sender;
    }


}
