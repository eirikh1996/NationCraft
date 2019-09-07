package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class InvitePlayerNationSubCommand extends NationSubCommand {
    private final String playerName;
    public InvitePlayerNationSubCommand(Player p, String playerName){
        super(p);
        this.playerName = playerName;

    }

    @Override
    public void execute() {
        Nation n = NationManager.getInstance().getNationByPlayer(sender);
        if (n == null) {
            sender.sendMessage(Messages.ERROR + "You are not in a nation!");
            return;
        }
        if (!PlayerManager.getInstance().playerIsAtLeast(sender, Ranks.OFFICER)) {
            sender.sendMessage("You must be at least officer to invite players");
            return;
        }
        UUID id;
        if (Bukkit.getPlayer(playerName) != null) {
            Player target = Bukkit.getPlayer(playerName);
            id = target.getUniqueId();
            target.sendMessage("You have been invited to join " + NationManager.getInstance().getColor(target, n) + n.getName());
        } else {
            id = PlayerManager.getInstance().getPlayerIDFromName(playerName);
        }
        if (id == null) {
            sender.sendMessage(Messages.ERROR + "Player " + playerName + " has never joined the server!");
            return;
        }
        n.invite(id);
        n.saveToFile();
    }
}
