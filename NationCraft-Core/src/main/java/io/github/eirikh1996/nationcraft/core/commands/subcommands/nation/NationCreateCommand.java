package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationCreateCommand extends Command {
    public NationCreateCommand(){
        super("create", List.of("c"));
        argument = "<name>";
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        if (!sender.hasPermission("nationcraft.nation.create")) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You do not have permission to use this command!", NamedTextColor.DARK_RED)));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must supply a name!", NamedTextColor.DARK_RED)));
            return;
        }
        if (args[0].length() <= 2) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Nation names must be at least 2 characters long")));
            return;
        }
        for (Nation existing : NationManager.getInstance().getNations()) {
            if (existing.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text(String.format("A nation with the name of %s already exists!", args[0]))));
                return;
            }
        }
        if (NationManager.getInstance().getNationByPlayer(player.getPlayerID()) != null) {
            sender.sendMessage(Messages.ERROR.append(Component.text("You must leave your current nation before you can create one!")));
            return;
        }
        if (NationSettings.CreateCost > 0 && !player.charge(NationSettings.CreateCost)) {

            return;
        }
        Nation newNation = NationManager.getInstance().createNation(name, player);
        if (newNation == null) {
            return;
        }
        sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("You successfully created a new nation named " ).append(newNation.getName(player))));
    }
}
