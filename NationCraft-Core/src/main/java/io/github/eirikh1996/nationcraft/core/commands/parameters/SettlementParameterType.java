package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;


public class SettlementParameterType extends AbstractParameterType<Settlement>{
    public SettlementParameterType() {
        super(Settlement.class);
    }

    @Override
    public Settlement readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty()) {//No input is given
            if (sender instanceof NCPlayer player) {//Check if a player sent the command
                Settlement playerSettlement = player.getSettlement();
                if (playerSettlement == null) {
                    sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are not in a settlement")));
                }
                return playerSettlement;
            }
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must supply a settlement name")));
            return null;
        }
        final Settlement settlement = SettlementManager.getInstance().getSettlementByName(input);
        if (settlement == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("No such settlement: " + input)));
        }
        return settlement;
    }
}
