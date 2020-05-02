package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationKickCommand extends Command {

    public NationKickCommand() {
        super("kick", "evict");
    }
    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a player!");
            return;
        }
        final NCPlayer target = PlayerManager.getInstance().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Invalid player name: " + args[0]);
            return;
        }
        if (!target.hasNation() || !player.getNation().equals(target.getNation())) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You can only kick members from your own nation!");
            return;
        }
        player.getNation().kickPlayer(target, player);
    }
}
