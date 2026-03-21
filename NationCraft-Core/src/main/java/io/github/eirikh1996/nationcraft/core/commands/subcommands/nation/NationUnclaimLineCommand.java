package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.IntegerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationUnclaimLineCommand extends Command {
    protected NationUnclaimLineCommand() {
        super("line", "l");
        addParameter("radius", new IntegerParameterType());
        addParameter("nation", new NationParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final int radius = getParameter("radius").getValue();
        final Nation target = getParameter("nation").getValue();
        if (target != player.getNation() && !player.hasPermission("nationcraft.nation.claim.other")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only claim for your own nation.")));
            return;
        }
        if (target == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are not in a nation!")));
            return;
        }

        target.unclaimTerritory(player, Shape.SQUARE, radius);
    }
}
