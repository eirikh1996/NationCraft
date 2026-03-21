package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.PlayerParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.StringParameterType;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationBanCommand extends Command {
    public NationBanCommand() {
        super("ban");
        addParameter("player", new PlayerParameterType());
        addParameter("reason", new StringParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        Nation nation = player.getNation();
        if (nation == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are not in a nation")));
            return;
        }
        NCPlayer target = getParameter("player").getValue();
        if (target == null) {
            return;
        }
        String reason = getParameter("reason").getValue();
        if (reason.isEmpty()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("you must supply a ban reason")));
            return;
        }
        String isBanned = nation.isBanned(target);
        if (isBanned != null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(Component.text("Player " + target.getName() + " is already banned from this nation for " + isBanned)));
            return;
        }
        nation.ban(target, reason);


    }
}
