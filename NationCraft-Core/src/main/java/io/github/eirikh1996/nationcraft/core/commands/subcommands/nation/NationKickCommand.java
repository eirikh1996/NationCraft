package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.PlayerParameterType;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationKickCommand extends Command {

    public NationKickCommand() {
        super("kick", "evict");
        addParameter("player", new PlayerParameterType(), true);
    }
    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final NCPlayer target = getParameter("player").getValue();
        if (target == null) {
            return;
        }
        if (!target.hasNation() || !player.getNation().equals(target.getNation())) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You can only kick members from your own nation!")));
            return;
        }
        player.getNation().kickPlayer(target, player);
    }
}
