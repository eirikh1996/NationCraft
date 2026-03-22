package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.SettlementParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.ShapeParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementClaimCommand extends Command {
    public SettlementClaimCommand() {
        super("claim", Arrays.asList("c"));
        addParameter("shape", new ShapeParameterType());
        addParameter("radius", new IntegerParameterType());
        addParameter("settlement", new SettlementParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final Shape shape = getParameter("shape").getValue();
        int radius = getParameter("radius").getValue();
        Settlement settlement = getParameter("settlement").getValue();
        if (settlement != player.getSettlement() && !sender.hasPermission("nationcraft.settlement.claim.other")){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only claim for your own settlement")));
            return;
        }
        if (settlement == null){
            return;
        }
        Nation pNation = player.getNation();
        Nation locNation = player.getLocation().getNation();
        if (locNation != pNation){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only claim for your settlement within the territory of your own nation")));
            return;
        }

        settlement.claimTerritory(player, shape, radius);
    }
}
