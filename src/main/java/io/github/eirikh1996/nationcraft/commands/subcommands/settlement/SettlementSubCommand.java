package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.commands.subcommands.SubCommand;
import org.bukkit.command.CommandSender;

public abstract class SettlementSubCommand extends SubCommand {
    public SettlementSubCommand(CommandSender sender) {
        super(sender);
    }

    @Override
    public abstract void execute();
}
