package io.github.eirikh1996.nationcraft.core.commands.parameters;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class PlayerParameterType extends AbstractParameterType<NCPlayer> {
    public PlayerParameterType() {
        super(NCPlayer.class);
    }

    @Override
    public NCPlayer readArgument(NCCommandSender sender, String input) {
        if (input.isEmpty() && sender instanceof NCPlayer you) {
            return you;
        }
        if (input.isEmpty()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must specify a player")));
            return null;
        }
        NCPlayer player = PlayerManager.getInstance().getPlayer(input);
        if (player == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Player " + input + " has never joined the server")));
        }
        return player;
    }
}
