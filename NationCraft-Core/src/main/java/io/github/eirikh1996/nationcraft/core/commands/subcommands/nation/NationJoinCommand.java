package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationJoinCommand extends Command {
    public NationJoinCommand() {
        super("join", Arrays.asList("j"));
        addParameter("nation", new NationParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        Nation nation = getParameter("nation").getValue();
        if (nation == null) {
            //sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR) + "You must specify a nation");
            return;
        }
        if (NationManager.getInstance().getNationByPlayer(player.getPlayerID()) != null) {
            sender.sendMessage(Messages.ERROR.append(Component.text("You must leave your nation before you can join another")));
            return;
        }
        String banReason = nation.isBanned(player);
        if (banReason != null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are banned from this nation for " + banReason)));
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
        if (nation.getPlayers().keySet().size() >= NationSettings.MaxPlayers) {
            sender.sendMessage(Messages.ERROR + "Nation " + nation.getName() + " is full! Join another nation, or create your own.");
            return;
        }
        if (nation.addPlayer(player)) {
            nation.getInvitedPlayers().remove(player);
            sender.sendMessage(String.format("You successfully joined %s", nation.getName()));
        } else {
            sender.sendMessage(String.format("You are already a member of %s", nation.getName()));
        }
    }
}
