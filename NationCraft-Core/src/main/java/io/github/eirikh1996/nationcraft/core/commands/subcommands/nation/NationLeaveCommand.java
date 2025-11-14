package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationLeaveCommand extends Command {

    public NationLeaveCommand(){
        super("leave", Arrays.asList("leave"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        Nation nation = NationManager.getInstance().getNationByPlayer(player.getPlayerID());
        if (nation == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }
        nation.removePlayer(player);
    }
}
