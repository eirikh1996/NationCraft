package io.github.eirikh1996.nationcraft.core.commands;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.subcommands.settlement.*;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class SettlementCommand extends Command {

    SettlementCommand() {
        super("settlement", Arrays.asList("s", "sm"));
        addChild(new SettlementClaimCommand());
        addChild(new SettlementCreateCommand());
        addChild(new SettlementInfoCommand());
        addChild(new SettlementInviteCommand());
        addChild(new SettlementJoinCommand());
        addChild(new SettlementListCommand());
        addChild(new SettlementNearestCommand());
        addChild(new SettlementSetTownCenterCommand());
        addChild(new SettlementUnclaimCommand());
    }
    @Override
    public void execute(NCCommandSender sender) {
        if (!(sender instanceof NCPlayer)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(MUST_BE_PLAYER));
            return;
        }
        if (!sender.hasPermission("nationcraft.settlement")){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX.append(ERROR).append(NO_PERMISSION));
            return;
        }
    }
}
