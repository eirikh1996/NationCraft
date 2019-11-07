package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class AllyNationSubCommand extends NationSubCommand {
    private final String name;
    public AllyNationSubCommand(Player sender, String name){
        super(sender);
        this.name = name;
    }
    @Override
    public void execute() {
        if (!sender.hasPermission("nationcraft.nation.ally")) {
            sender.sendMessage("Error: You do not have permission to use this command!");
        }
        NationManager nMgr = new NationManager();

        Nation ownNation = nMgr.getNationByPlayer(sender.getUniqueId()); //sender's own nation
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
            for (NCPlayer player : allyNation.getPlayers().keySet()) {
                Player p = Bukkit.getPlayer(player.getPlayerID());
                if (p == null) {
                    continue;
                }
                p.sendMessage(ownNation.getName() + " wishes to be an allied nation. Use command /nation ally " + ownNation.getName() + " to confirm an alliance.");
            }
        }
        ownNation.addAlly(allyNation);
        ownNation.saveToFile();
    }

}
