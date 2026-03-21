package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.commands.parameters.BooleanParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.NationParameterType;
import io.github.eirikh1996.nationcraft.core.commands.parameters.StringParameterType;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import net.kyori.adventure.text.Component;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NationFlagSetCommand extends Command {
    protected NationFlagSetCommand() {
        super("set");
        addParameter("flag", new StringParameterType(), true);
        addParameter("value", new BooleanParameterType(), true);
        addParameter("nation", new NationParameterType());
    }

    @Override
    protected void execute(NCCommandSender sender) {
        String flag = getParameter("flag").getValue();
        if (flag.isEmpty()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You need to specify a flag")));
            return;
        }
        if (!NationManager.getInstance().registeredFlag(flag)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Invalid flag: " + flag, ERROR.color())));
            return;
        }
        Boolean flagValue = getParameter("value").getValue();
        if (flagValue == null) {
            //sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must specify either true or false", ERROR.color())));
            return;
        }

        Nation target = getParameter("nation").getValue();
        if (target == null) {
            //sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text((args.length > 3 ? "Invalid nation name: " + args[1] : "You must specify a nation"), ERROR.color())));
            return;
        }
        target.setFlag(flag.toLowerCase(), flagValue);
    }
}
