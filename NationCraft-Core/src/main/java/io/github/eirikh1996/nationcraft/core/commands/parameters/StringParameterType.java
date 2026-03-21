package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

public class StringParameterType extends AbstractParameterType<String> {
    public StringParameterType() {
        super(String.class);
    }

    @Override
    public String readArgument(NCCommandSender sender, String input) {
        return input;
    }
}
