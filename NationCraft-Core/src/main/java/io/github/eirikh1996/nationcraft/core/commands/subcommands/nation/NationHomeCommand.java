package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationHomeCommand extends Command {
    public NationHomeCommand() {
        super("home", "capital", "h");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        if (!player.hasNation()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(NOT_IN_A_NATION));
            return;
        }
        final Nation pNation = player.getNation();
        if (!pNation.hasCapital()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR)+ "Your nation has no settlements and thus, no capital. Create a settlement with /s create <name>. This will automatically be assigned as capital of your nation");
            return;
        }
        NCLocation destination = pNation.getCapital().getTownCenter().getTeleportationPoint();

        player.teleport(
                destination,
                NATIONCRAFT_COMMAND_PREFIX + "Teleporting to " + pNation.getCapital().getName() + ", the capital of " + pNation.getName(player) + " in " + Settings.player.TeleportationWarmup + ". Do not move while warming up!",
                NATIONCRAFT_COMMAND_PREFIX + "Teleporting to " + pNation.getCapital().getName() + ", the capital of " + pNation.getName(player));
    }
}
