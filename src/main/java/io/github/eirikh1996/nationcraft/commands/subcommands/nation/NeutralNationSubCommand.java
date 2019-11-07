package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class NeutralNationSubCommand extends NationSubCommand {
    private final String name;
    public NeutralNationSubCommand(Player sender, String name){
        super(sender);
        this.name = name;
    }
    @Override
    public void execute() {
        NationManager nMgr = NationManager.getInstance();
        Nation ownNation = nMgr.getNationByPlayer(sender.getUniqueId()); //sender's own nation
        Nation neutralNation = nMgr.getNationByName(name); //nation to ally
        if (ownNation.getAllies().contains(neutralNation.getName())) {
            ownNation.removeAlly(neutralNation);
            //if the sender's nation is on the neutral nations enemy list
            if (neutralNation.getEnemies().contains(ownNation.getName())) {
                for (NCPlayer np : neutralNation.getPlayers().keySet()) {
                    Player p = Bukkit.getPlayer(np.getPlayerID());
                    if (p == null) {
                        continue;
                    }
                    p.sendMessage(ownNation.getName() + " wishes to be a neutral nation. Type /nation neutral " + ownNation.getName() + " to confirm neutrality.");
                }
                ownNation.removeEnemy(neutralNation);
            } else {
                sender.sendMessage(neutralNation.getName() + " is now a neutral nation");
            }
            //&& !neutralNation.getAllies().contains(ownNation.getName())
        } else if (ownNation.getEnemies().contains(neutralNation.getName())) {
            ownNation.removeEnemy(neutralNation);

        } else {
            sender.sendMessage(neutralNation.getName() + " is already neutral");
            return;
        }
        ownNation.saveToFile();
    }
}
