package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.player.PlayerAdminCommand;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.player.PlayerInfoCommand;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.player.PlayerPowerCommand;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class PlayerCommand extends Command {

    PlayerCommand() {
        super("player", Arrays.asList("p", "ncplayer", "ncp"));
        addChild(new PlayerAdminCommand());
        addChild(new PlayerPowerCommand());
        addChild(new PlayerInfoCommand());
    }

    @Override
    public void execute(NCCommandSender sender) {

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
