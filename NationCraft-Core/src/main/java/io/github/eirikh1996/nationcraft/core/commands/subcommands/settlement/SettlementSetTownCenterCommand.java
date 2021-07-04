package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.api.territory.TownCenter;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class SettlementSetTownCenterCommand extends Command {

    public SettlementSetTownCenterCommand() {
        super("settowncenter");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        NCPlayer player = (NCPlayer) sender;
        Settlement settlement;
        if (args.length > 0){
            if (!sender.hasPermission("nationcraft.settlement.settowncenter.other")){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only set town center for your own settlement");
                return;
            }
            settlement = SettlementManager.getInstance().getSettlementByName(args[0]);
        } else {
            settlement = SettlementManager.getInstance().getSettlementByPlayer(player);
        }
        if (settlement == null){
            if (args.length > 0){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "No settlement named " + args[0] + " exist");
                return;
            }
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a settlement");
            return;
        }
        Settlement atLoc = SettlementManager.getInstance().getSettlementAt(player.getLocation());
        if (atLoc == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Town center must be within the territory of your settlement");
            return;
        } else if (!atLoc.equals(settlement)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot set town center in another settlement");
            return;
        }
        settlement.setTownCenter(new TownCenter(player.getLocation().getBlockX() >> 4, player.getLocation().getBlockZ() >> 4, player.getLocation().getWorld(), player.getLocation()));
        settlement.saveToFile();
    }
}
