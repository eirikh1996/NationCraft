package io.github.eirikh1996.nationcraft.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class InfoSettlementSubCommand extends SettlementSubCommand {
    private final String settlementName;

    public InfoSettlementSubCommand(Player sender, String settlementName) {
        super(sender);
        this.settlementName = settlementName;
    }

    @Override
    public void execute() {
        Settlement settlement = settlementName.length() > 0 ? SettlementManager.getInstance().getSettlementByName(settlementName) : SettlementManager.getInstance().getSettlementByPlayer(sender.getUniqueId());
        if (settlement == null){
            if (settlementName.length() > 0){
                sender.sendMessage(settlementName + " does not exist");
                return;
            }
            sender.sendMessage("You are not member of a settlement and did not specify a settlement");
            return;
        }
        Nation owner = NationManager.getInstance().getNationBySettlement(settlement);
        String exposure = "";
        if (settlement.getExposurePercent() >= Settings.MinimumSettlementExposurePercent){
            exposure += ChatColor.RED;
        } else if (settlement.getExposurePercent() >= Settings.MinimumSettlementExposurePercent - (Settings.MinimumSettlementExposurePercent / 3)){
            exposure += ChatColor.YELLOW;
        } else {
            exposure += ChatColor.GREEN;
        }
        exposure += settlement.getExposurePercent();
        sender.sendMessage("================{" + settlement.getName() + "}====================");
        sender.sendMessage("Nation: " + owner.getName() + " " + " " + (owner.getCapital() != null && owner.getCapital().equals(settlement) ? " (capital)" : ""));
        sender.sendMessage("Mayor: " + settlement.getMayor().getName());
        sender.sendMessage("Exposure: " + exposure);
    }
}
