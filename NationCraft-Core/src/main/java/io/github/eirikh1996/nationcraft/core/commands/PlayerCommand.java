package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.player.PlayerAdminCommand;
import io.github.eirikh1996.nationcraft.core.messages.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class PlayerCommand extends Command {

    PlayerCommand() {
        super("player", Arrays.asList("p"));
        addChild(new PlayerAdminCommand());
    }

    @Override
    public void execute(NCCommandSender sender, String[] args) {
        if (sender instanceof NCPlayer && args.length == 0) {
            NCPlayer player = (NCPlayer) sender;
            Messages.displayPlayerInfo(player);
            return;
        }
        else if (args.length > 0 && children.containsKey(args[0])) {
            children.get(args[0]).execute(sender, Arrays.copyOfRange(args, 1, args.length));
            return;
        } else if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a player name or a sub command");
            return;
        }
        NCPlayer player = PlayerManager.getInstance().getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Invalid player name: " + args[0]);
            return;
        }
        Messages.displayPlayerInfo(sender, player);
    }

    @Override
    public List<String> getTabCompletions(NCCommandSender sender, String[] args) {
        final List<String> completions = super.getTabCompletions(sender, args);
        for (NCPlayer player : PlayerManager.getInstance()) {
            completions.add(player.getName());
        }
        return completions;
    }
}
