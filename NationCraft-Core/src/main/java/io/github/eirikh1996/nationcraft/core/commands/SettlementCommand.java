package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.messages.Messages;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class SettlementCommand extends Command {

    SettlementCommand() {
        super("settlement", Arrays.asList("s", "sm"));
    }
    @Override
    public void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + Messages.MUST_BE_PLAYER);
            return;
        }
        if (!sender.hasPermission("nationcraft.settlement")){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + Messages.NO_PERMISSION);
            return;
        }
        if (args.length == 0){
            return;
        }
        children.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }
}
