package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.SettlementParameterType;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class SettlementUnclaimAllCommand extends Command {

    public SettlementUnclaimAllCommand() {
        super("all");
        addParameter("settlement", new SettlementParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        Settlement settlement = getParameter("settlement").getValue();
        if (settlement != player.getSettlement() && !player.hasPermission("nationcraft.settlement.unclaim.other")){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only unclaim for your own settlement")));
            return;
        }
        if (settlement == null){
            return;
        }

        settlement.unclaimTerritory(player, Shape.ALL, 0);
    }
}
