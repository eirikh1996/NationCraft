package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class LeaveNationSubCommand extends NationSubCommand {

    public LeaveNationSubCommand(Player sender){
        super(sender);
    }
    @Override
    public void execute() {
        Nation nation = NationManager.getInstance().getNationByPlayer(sender);
        if (nation == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }
        if (nation.getPlayers().keySet().remove(sender.getUniqueId())) {
            sender.sendMessage("You have left your nation");
            for (NCPlayer np : nation.getPlayers().keySet()) {
                Player p = Bukkit.getPlayer(np.getPlayerID());
                if (p == null) {
                    continue;
                }
                p.sendMessage(String.format("%s left your nation.", p.getName()));
            }
        }
        if (nation.getPlayers().keySet().isEmpty()) {
            final String nName = nation.getName();
            if (NationManager.getInstance().deleteNation(nation)) {
                Bukkit.broadcastMessage(String.format("Nation %s has been disbanded", nName));
            }
        }
        nation.saveToFile();
    }
}
