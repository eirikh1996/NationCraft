package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.events.nation.NationCreateEvent;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationCreateCommand extends Command {
    public NationCreateCommand(){
        super("create", Arrays.asList("c"));
        argument = "<name>";
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (!sender.hasPermission("nationcraft.nation.create")) {
            sender.sendMessage("Error: You do not have permission to use this command!");
            return;
        }
        if (args.length == 0) {
            sender.sendMessage("Error: You must supply a name!");
            return;
        }
        if (args[0].length() <= 2) {
            sender.sendMessage("Error: Nation names must be at least 2 characters long");
            return;
        }
        for (Nation existing : NationManager.getInstance().getNations()) {
            if (existing.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage(String.format("Error: A nation with the name of %s already exists!", args[0]));
                return;
            }
        }
        final NCPlayer player = (NCPlayer) sender;
        if (NationManager.getInstance().getNationByPlayer(player.getPlayerID()) != null) {
            sender.sendMessage(Messages.ERROR + "You must leave your current nation before you can create one!");
            return;
        }
        if (NationSettings.CreateCost > 0 && !player.charge(NationSettings.CreateCost)) {

            return;
        }
        Nation newNation = new Nation(args[0], player);
        //Call event
        NationCreateEvent event = new NationCreateEvent(newNation, player);
        NationCraftAPI.getInstance().callEvent(event);
        if (event.isCancelled()) {
            sender.sendMessage("Cancelled: " + event.isCancelled());
            return;
        }
        sender.sendMessage("You successfully created a new nation named " + TextColor.GREEN + args[0]);
        NationManager.getInstance().getNations().add(newNation);
        newNation.saveToFile();
    }
}
