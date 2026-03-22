package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.core.commands.parameters.SettlementParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.settlement.Ranks;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementInfoCommand extends Command {

    public SettlementInfoCommand() {
        super("info");
        addParameter("settlement", new SettlementParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        Settlement settlement = getParameter("settlement").getValue();
        if (settlement == null){
            return;
        }
        Nation owner = NationManager.getInstance().getNationBySettlement(settlement);
        TextComponent exposure = getExposure(settlement);

        List<String> members = new ArrayList<>();
        for (Map.Entry<NCPlayer, Ranks> entry : settlement.getPlayers().entrySet()) {
            if (entry.getValue() == Ranks.MAYOR) continue;
            members.add("[" + entry.getValue().name().toLowerCase() + "] " + entry.getKey().getName());
        }

        sender.sendMessage(Component.text("================{",NamedTextColor.DARK_GRAY).append(Component.text(settlement.getName(), NamedTextColor.AQUA)).append(Component.text("}====================", NamedTextColor.DARK_GRAY)));
        sender.sendMessage(Component.text("Nation: ", NamedTextColor.DARK_AQUA).append(Component.text(owner.getName() + " " + (owner.getCapital() != null && owner.getCapital().equals(settlement) ? "(capital)" : ""), NamedTextColor.AQUA)));
        sender.sendMessage(Component.text("Mayor: ", NamedTextColor.DARK_AQUA).append(Component.text(settlement.getMayor().getName(), NamedTextColor.AQUA)));
        sender.sendMessage(Component.text("Exposure: ", NamedTextColor.DARK_AQUA).append(exposure));
        sender.sendMessage(Component.text("Members: ", NamedTextColor.DARK_AQUA).append(Component.text(String.join(", ", members), NamedTextColor.AQUA)));
        sender.sendMessage(Component.text("Territory: ", NamedTextColor.DARK_AQUA).append(Component.text(settlement.getTerritory().size(), NamedTextColor.AQUA)).append(Component.text("/", NamedTextColor.DARK_AQUA)).append(Component.text(settlement.getMaxTerritory(), NamedTextColor.AQUA)));
    }

    private static @NotNull TextComponent getExposure(Settlement settlement) {
        TextColor exposureColor;
        if (settlement.getExposurePercent() >= Settings.MinimumSettlementExposurePercent){
            exposureColor = NamedTextColor.RED;
        } else if (settlement.getExposurePercent() >= Settings.MinimumSettlementExposurePercent - (Settings.MinimumSettlementExposurePercent / 3)){
            exposureColor = NamedTextColor.YELLOW;
        } else {
            exposureColor = NamedTextColor.GREEN;
        }
        return Component.text(settlement.getExposurePercent(), exposureColor);
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
