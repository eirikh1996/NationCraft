package io.github.eirikh1996.nationcraft.core.commands.subcommands.map;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class MapHeightSubCommand extends Command {
    public MapHeightSubCommand() {
        super("height", Arrays.asList("h", "size", "s"));
    }

    @Override
    public void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer) ) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        NCPlayer player = (NCPlayer) sender;
        int height = Integer.parseInt(args[0]);
        if (height > 20) {
            sender.sendMessage(ERROR + "Max map height cannot exceed 20");
            return;
        }
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Map height set to " + height);
        player.setMapHeight(height);
    }
}
