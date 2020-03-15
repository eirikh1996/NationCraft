package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationInfoCommand extends Command {
    public NationInfoCommand() {
        super("info", Arrays.asList("i"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        Nation n;
        NCPlayer player = (NCPlayer) sender;
        if (args.length == 0) {
            n = NationManager.getInstance().getNationByPlayer(player.getPlayerID());
        } else {
            n = NationManager.getInstance().getNationByName(name);
        }
        if (n == null) {
            sender.sendMessage(Messages.ERROR + "Nation does not exist!");
            return;
        }
        TextColor color = NationManager.getInstance().getColor(player, n);
        Messages.nationInfo(n, player, color);
    }

    @Override
    public List<String> getTabCompletions(final NCCommandSender sender, final String[] args) {
        List<String> completions = new ArrayList<>();
        for (Nation n : NationManager.getInstance()) {
            completions.add(n.getName());
        }
        return completions;
    }
}
