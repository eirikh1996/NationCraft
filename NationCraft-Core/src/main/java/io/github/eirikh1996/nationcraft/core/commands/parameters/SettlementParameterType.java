package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;



public class SettlementParameterType extends AbstractParameterType<Settlement>{
    public SettlementParameterType(Class<Settlement> typeClass, TextComponent errorMessage) {
        super(typeClass, errorMessage);
    }

    @Override
    public Settlement readArgument(NCCommandSender sender, String input) {
        return SettlementManager.getInstance().getSettlementByName(input);
    }
}
