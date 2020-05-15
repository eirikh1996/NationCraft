package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationNeutralCommand extends Command {

    public NationNeutralCommand(){
        super("neutral");
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
        NationManager nMgr = NationManager.getInstance();
        Nation ownNation = nMgr.getNationByPlayer(player); //sender's own nation
        Nation neutralNation = nMgr.getNationByName(args[0]); //nation to ally
        if (ownNation.getAllies().contains(neutralNation)) {
            ownNation.removeAlly(neutralNation);
            //if the sender's nation is on the neutral nations enemy list
            if (neutralNation.getEnemies().contains(ownNation)) {
                for (NCPlayer p : neutralNation.getPlayers().keySet()) {
                    if (!p.isOnline()) {
                        continue;
                    }
                    p.sendMessage(ownNation.getName() + " wishes to be a neutral nation. Type /nation neutral " + ownNation.getName() + " to confirm neutrality.");
                }
                ownNation.removeEnemy(neutralNation);
            } else {
                sender.sendMessage(neutralNation.getName() + " is now a neutral nation");
            }
            //&& !neutralNation.getAllies().contains(ownNation.getName())
        } else if (ownNation.getEnemies().contains(neutralNation)) {
            ownNation.removeEnemy(neutralNation);

        } else {
            sender.sendMessage(neutralNation.getName() + " is already neutral");
            return;
        }
        ownNation.saveToFile();
    }
}
