package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementInviteCommand extends Command {

    public SettlementInviteCommand() {
        super("invite", "i");
    }
    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer player)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        final Settlement ps = player.getSettlement();
        if (ps == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You are not in a settlement")));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("You must specify a player")));
            return;
        }
        final NCPlayer target = PlayerManager.getInstance().getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(Component.text("Invalid player: " + args[0])));
            return;
        }
        ps.invite(player, target);

    }

    @Override
    public List<String> getTabCompletions(NCCommandSender sender, String[] args) {
        final List<String> completions = new ArrayList<>();
        for (NCPlayer p : PlayerManager.getInstance()) {
            completions.add(p.getName());
        }
        return completions;
    }
}
