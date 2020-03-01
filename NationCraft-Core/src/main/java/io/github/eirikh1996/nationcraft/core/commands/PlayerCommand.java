package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class PlayerCommand extends Command {

    PlayerCommand() {
        super("player", Arrays.asList("p"));
    }

    @Override
    public void execute(NCCommandSender sender, String[] args) {
        if ((!(sender instanceof NCPlayer)) && args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a player");
            return;
        }
        children.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }
}
