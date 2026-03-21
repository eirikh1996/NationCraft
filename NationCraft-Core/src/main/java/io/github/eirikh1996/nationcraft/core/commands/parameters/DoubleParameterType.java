package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

public class DoubleParameterType extends AbstractParameterType<Double> {
    public DoubleParameterType() {
        super(Double.class);
    }

    @Override
    public Double readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty()) {
            return -1D;
        }
        return Double.parseDouble(input);
    }
}
