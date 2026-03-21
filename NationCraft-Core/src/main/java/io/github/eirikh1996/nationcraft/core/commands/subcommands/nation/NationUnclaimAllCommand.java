package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.territory.Territory;
import io.github.eirikh1996.nationcraft.core.claiming.Shape;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationUnclaimAllCommand extends Command {
    protected NationUnclaimAllCommand() {
        super("all");
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
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only claim for your own nation.")));
            return;
        }
        if (target == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are not in a nation!")));
            return;
        }

        target.unclaimTerritory(player, Shape.ALL, 1);
    }
}
