package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.commands.subcommands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SettlementSubCommand extends SubCommand {
    public SettlementSubCommand(Player sender) {
        super(sender);
    }

    @Override
    public abstract void execute();
}
