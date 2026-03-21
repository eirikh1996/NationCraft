package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;

import java.util.HashSet;
import java.util.Set;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationUnclaimSingleCommand extends Command {
    protected NationUnclaimSingleCommand() {
        super("single");
        addParameter("nation", new NationParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final Nation target = getParameter("nation").getValue();
        Set<Territory> unclaimedTerritory = new HashSet<>();
        if (target != player.getNation() && !player.hasPermission("nationcraft.nation.claim.other")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You can only claim for your own nation.");
            return;
        }
        if (target == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You are not in a nation!");
            return;
        }

        target.unclaimTerritory(player, Shape.SINGLE, 1);
    }
}
