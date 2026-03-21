package io.github.eirikh1996.nationcraft.core.commands.subcommands.player;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.PlayerParameterType;
import io.github.eirikh1996.nationcraft.core.messages.Messages;

public class PlayerInfoCommand extends Command {
    public PlayerInfoCommand() {
        super("info", "i");
        addParameter("player", new PlayerParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        NCPlayer player = getParameter("player").getValue();
        if (player == null) {
            return;
        }
        Messages.displayPlayerInfo(sender, player);
    }
}
