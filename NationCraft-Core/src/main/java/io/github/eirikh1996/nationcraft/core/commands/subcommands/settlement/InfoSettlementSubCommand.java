package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class InfoSettlementSubCommand extends Command {

    public InfoSettlementSubCommand() {
        super("info");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        NCPlayer player = (NCPlayer) sender;
        Settlement settlement = args.length > 0 ? SettlementManager.getInstance().getSettlementByName(args[0]) : SettlementManager.getInstance().getSettlementByPlayer(player);
        if (settlement == null){
            if (args[0].length() > 0){
                sender.sendMessage(args[0] + " does not exist");
                return;
            }
            sender.sendMessage("You are not member of a settlement and did not specify a settlement");
            return;
        }
        Nation owner = NationManager.getInstance().getNationBySettlement(settlement);
        String exposure = "";
        if (settlement.getExposurePercent() >= Settings.MinimumSettlementExposurePercent){
            exposure += TextColor.RED;
        } else if (settlement.getExposurePercent() >= Settings.MinimumSettlementExposurePercent - (Settings.MinimumSettlementExposurePercent / 3)){
            exposure += TextColor.YELLOW;
        } else {
            exposure += TextColor.GREEN;
        }
        exposure += settlement.getExposurePercent();
        sender.sendMessage("================{" + settlement.getName() + "}====================");
        sender.sendMessage("Nation: " + owner.getName() + " " + " " + (owner.getCapital() != null && owner.getCapital().equals(settlement) ? " (capital)" : ""));
        sender.sendMessage("Mayor: " + settlement.getMayor().getName());
        sender.sendMessage("Exposure: " + exposure);
    }
}
