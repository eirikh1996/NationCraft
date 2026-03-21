package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.core.commands.parameters.BooleanParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.StringParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationFlagCommand extends Command {
    public NationFlagCommand() {
        super("flag");
        addChild(new NationFlagListCommand());
        addChild(new NationFlagSetCommand());
    }

    @Override
    public void execute(NCCommandSender sender) {
        String action = getParameter("action").getValue();
        if (action.isEmpty()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Usage: /nation flag <list|set>")));
        }
    }
}
