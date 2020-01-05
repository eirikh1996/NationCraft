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
        Nation nation = NationManager.getInstance().getNationByPlayer(sender.getUniqueId());
        if (nation == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }
        nation.removePlayer(sender.getUniqueId());
        nation.saveToFile();
    }
}
