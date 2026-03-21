package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.TextComponent;

public class IntegerParameterType extends AbstractParameterType<Integer> {
    public IntegerParameterType() {
        super(Integer.class);
    }

    @Override
    public Integer readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(input);
    }
}
