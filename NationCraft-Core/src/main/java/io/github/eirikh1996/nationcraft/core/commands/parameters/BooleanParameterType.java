package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.NationCraftCommandException;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class BooleanParameterType extends AbstractParameterType<Boolean> {
    public BooleanParameterType() {
        super(Boolean.class);
    }

    @Override
    public Boolean readArgument(NCCommandSender sender, String input) {
        if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Boolean values must be either true or false")) );
            return null;
        }
        return input.equalsIgnoreCase("true");
    }
}
