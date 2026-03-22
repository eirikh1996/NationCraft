package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementUnclaimCommand extends Command {

    public SettlementUnclaimCommand() {
        super("unclaim");
        addChild(new SettlementUnclaimAllCommand());
        addChild(new SettlementUnclaimCircleCommand());
        addChild(new SettlementUnclaimLineCommand());
        addChild(new SettlementUnclaimSquareCommand());
        addChild(new SettlementUnclaimSingleCommand());
    }

    @Override
    protected void execute(NCCommandSender sender) {

    }
}
