package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.parameters.SettlementParameterType;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.api.territory.TownCenter;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class SettlementSetTownCenterCommand extends Command {

    public SettlementSetTownCenterCommand() {
        super("settowncenter");
        addParameter("settlement", new SettlementParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        Settlement settlement = getParameter("settlement").getValue();
        if (settlement != player.getSettlement() && !sender.hasPermission("nationcraft.settlement.settowncenter.other")){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only set town center for your own settlement")));
            return;
        }
        if (settlement == null){
            //sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are not in a settlement")));
            return;
        }
        Settlement atLoc = SettlementManager.getInstance().getSettlementAt(player.getLocation());
        if (atLoc == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Town center must be within the territory of your settlement")));
            return;
        } else if (!atLoc.equals(settlement)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You cannot set town center in another settlement")));
            return;
        }
        settlement.setTownCenter(new TownCenter(player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4, player.getLocation().getWorld(), player.getLocation()));
        settlement.saveToFile();
    }
}
