package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationJoinCommand extends Command {
    public NationJoinCommand() {
        super("join", Arrays.asList("j"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a nation");
            return;
        }
        Nation nation = NationManager.getInstance().getNationByName(name);
        if (nation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Nation %s does not exist", name));
            return;
        }
        if (NationManager.getInstance().getNationByPlayer(player.getPlayerID()) != null) {
            sender.sendMessage(Messages.ERROR + "You must leave your nation before you can join another");
            return;
        }
        if (!nation.isOpen() && !nation.getInvitedPlayers().contains(player)) {
            sender.sendMessage("This nation requires invitation");
            for (NCPlayer np : nation.getPlayers().keySet()) {
                if (!player.isOnline()) {
                    continue;
                }
                player.sendMessage(String.format("%s tried to join your nation.", player.getName()));
            }
            return;
        }
        if (nation.getPlayers().keySet().size() >= Settings.NationMaxPlayers) {
            sender.sendMessage(Messages.ERROR + "Nation " + nation.getName() + " is full! Join another nation, or create your own.");
            return;
        }
        if (nation.addPlayer(player)) {
            nation.getInvitedPlayers().remove(player);
            sender.sendMessage(String.format("You successfully joined %s", nation.getName()));
        } else {
            sender.sendMessage(String.format("You are already a member of %s", nation.getName()));
        }
        nation.saveToFile();
    }
}
