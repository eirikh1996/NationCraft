package io.github.eirikh1996.nationcraft.core.commands.subcommands.player;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class PlayerAdminCommand extends Command {
    public PlayerAdminCommand() {
        super("admin", Arrays.asList("override"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!sender.hasPermission("nationcraft.player.adminmode")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(NO_PERMISSION));
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        player.setAdminMode(!player.isAdminMode());
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Admin mode " +(player.isAdminMode() ? "en" : "dis") + "abled")));
    }
}
