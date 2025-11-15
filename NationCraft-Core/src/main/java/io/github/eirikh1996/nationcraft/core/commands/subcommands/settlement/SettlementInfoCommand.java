package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.settlement.Ranks;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementInfoCommand extends Command {

    public SettlementInfoCommand() {
        super("info");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer player) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        Settlement settlement = args.length > 0 ? SettlementManager.getInstance().getSettlementByName(args[0]) : SettlementManager.getInstance().getSettlementByPlayer(player);
        if (settlement == null){
            if (args.length > 0){
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
        List<String> members = new ArrayList<>();
        for (Map.Entry<NCPlayer, Ranks> entry : settlement.getPlayers().entrySet()) {
            if (entry.getValue() == Ranks.MAYOR) continue;
            members.add("[" + entry.getValue().name().toLowerCase() + "] " + entry.getKey().getName());
        }
        sender.sendMessage("§8================{§b" + settlement.getName() + "§8}====================");
        sender.sendMessage("§3Nation: §b" + owner.getName() + " " + (owner.getCapital() != null && owner.getCapital().equals(settlement) ? "(capital)" : ""));
        sender.sendMessage("§3Mayor: §b" + settlement.getMayor().getName());
        sender.sendMessage("§3Exposure: §b" + exposure);
        sender.sendMessage("§3Members: §b" + String.join(", ", members));
        sender.sendMessage("§3Territory: §b" + settlement.getTerritory().size() + "§3/§b" + settlement.getMaxTerritory());
    }

    @Override
    public List<String> getTabCompletions(NCCommandSender sender, String[] args) {
        final List<String> completions = new ArrayList<>();
        for (Settlement settlement : SettlementManager.getInstance()) {
            completions.add(settlement.getName());
        }
        return completions;
    }
}
