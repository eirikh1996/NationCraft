package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.territory.TownCenter;
import org.bukkit.entity.Player;

import static io.github.eirikh1996.nationcraft.messages.Messages.*;

public final class SetTownCenterSettlementSubCommand extends SettlementSubCommand {
    private final String settlementName;
    public SetTownCenterSettlementSubCommand(Player sender, String settlementName) {
        super(sender);
        this.settlementName = settlementName;
    }

    @Override
    public void execute() {
        Settlement settlement;
        if (settlementName.length() > 0){
            if (!sender.hasPermission("nationcraft.settlement.settowncenter.other")){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only set town center for your own settlement");
                return;
            }
            settlement = SettlementManager.getInstance().getSettlementByName(settlementName);
        } else {
            settlement = SettlementManager.getInstance().getSettlementByPlayer(sender.getUniqueId());
        }
        if (settlement == null){
            if (settlementName.length() > 0){
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "No settlement named " + settlementName + " exist");
                return;
            }
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a settlement");
            return;
        }
        Settlement atLoc = SettlementManager.getInstance().getSettlementAt(sender.getLocation());
        if (atLoc == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Town center must be within the territory of your settlement");
            return;
        } else if (!atLoc.equals(settlement)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot set town center in another settlement");
            return;
        }
        settlement.setTownCenter(new TownCenter(sender.getLocation().getChunk().getX(), sender.getLocation().getChunk().getZ(), sender.getWorld(), sender.getLocation()));
        settlement.saveToFile();
    }
}
