package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.messages.Messages;
import org.bukkit.entity.Player;

public class NearestSettlementSubCommand extends SettlementSubCommand {
    public NearestSettlementSubCommand(Player sender) {
        super(sender);
    }

    @Override
    public void execute() {
        Messages.nearestSettlements(sender);
    }
}
