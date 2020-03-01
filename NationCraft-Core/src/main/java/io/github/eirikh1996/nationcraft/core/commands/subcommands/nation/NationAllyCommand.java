package io.github.eirikh1996.nationcraft.core.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.core.commands.Command;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;

import java.util.Arrays;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.*;

public final class NationAllyCommand extends Command {
    public NationAllyCommand(){
        super("ally", Arrays.asList("a"));
    }

    @Override
    protected void execute(NCCommandSender sender, String[] args) {
        if (!(sender instanceof NCPlayer)) {
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + MUST_BE_PLAYER);
            return;
        }
        if (!sender.hasPermission("nationcraft.nation.ally")) {
            sender.sendMessage("Error: You do not have permission to use this command!");
        }
        NationManager nMgr = NationManager.getInstance();
        final NCPlayer player = (NCPlayer) sender;
        Nation ownNation = nMgr.getNationByPlayer(player.getPlayerID()); //sender's own nation
        Nation allyNation = nMgr.getNationByName(name); //nation to ally
        if (allyNation == null) {
            sender.sendMessage("The given nation does not exist");
            return;
        }
        if (ownNation.isAlliedWith(allyNation)) {
            sender.sendMessage("You have already set this relation wish with " + allyNation.getName());
            return;
        }
        if (allyNation.getAllies().contains(ownNation.getName())) {
            sender.sendMessage(allyNation.getName() + "is now an allied nation");
        } else {
            for (NCPlayer p : allyNation.getPlayers().keySet()) {
                if (!p.isOnline()) {
                    continue;
                }
                p.sendMessage(ownNation.getName() + " wishes to be an allied nation. Use command /nation ally " + ownNation.getName() + " to confirm an alliance.");
            }
        }
        ownNation.addAlly(allyNation);
        ownNation.saveToFile();
    }
}
