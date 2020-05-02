package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public class NationWarCommand extends Command {

    public NationWarCommand() {
        super("war");
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You must specify a nation");
            return;
        }
        final NCPlayer player = (NCPlayer) sender;
        Nation pNation = NationManager.getInstance().getNationByPlayer(player);
        Nation enemy = NationManager.getInstance().getNationByName(args[0]);
        if (pNation == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a nation");
            return;
        }
        else if (enemy == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Nation %s does not exist", args[0]));
            return;
        } else if (pNation.isAtWarWith(enemy)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Your nation is already at war with %s", args[0]));
            return;
        }
        if (pNation.getAllies().contains(enemy)){
            pNation.removeAlly(enemy);
        }
        pNation.addEnemy(enemy);
        for (NCPlayer p : pNation.getPlayers().keySet()){
            if (!p.isOnline()){
                continue;
            }
            p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("%s is now a hostile nation", enemy.getName(pNation)));
        }
        for (NCPlayer p : enemy.getPlayers().keySet()){
            if (!p.isOnline()){
                continue;
            }
            p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("%s is now a hostile nation", pNation.getName(enemy)));
        }
    }
}