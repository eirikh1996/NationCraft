package io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementJoinCommand extends Command {

    public SettlementJoinCommand() {
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
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a settlement");
            return;
        }
        final Settlement settlement = SettlementManager.getInstance().getSettlementByName(args[0]);
        if (settlement == null) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Settlement " + args[0] + " does not exist!");
            return;
        }
        Nation sn = settlement.getNation();
        if (!player.hasNation()) {
            String banReason = sn.isBanned(player);
            if (banReason != null) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You cannot join this settlement because you are banned from the nation " + sn.getName(player) + ", who owns " + settlement.getName() + " for " + banReason);
                return;
            }
            if (!sn.isInvited(player) && !settlement.isInvited(player)) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Both settlement " + settlement.getName() + " and the nation owning it, " + sn.getName() + ", requires invitation!");
                return;
            }
            else if (!sn.isInvited(player)) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Nation " + sn.getName() + ", who owns settlement " + settlement.getName() + " requires invitation!");
                return;
            }
            else if (!settlement.isInvited(player)) {
                sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Settlement " + settlement.getName() + " requires invitation!");
                return;
            }
            sn.addPlayer(player);
        }
        if (player.hasSettlement()) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must leave your current settlement to join a new one");
            return;
        }
        settlement.addPlayer(player);
    }
}
