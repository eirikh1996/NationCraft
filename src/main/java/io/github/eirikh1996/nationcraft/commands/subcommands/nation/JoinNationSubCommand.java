package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class JoinNationSubCommand extends NationSubCommand {
    private final String name;
    public JoinNationSubCommand(Player p, String name){
        super(p);
        this.name = name;
    }
    @Override
    public void execute() {
        Nation nation = NationManager.getInstance().getNationByName(name);
        if (nation == null) {
            sender.sendMessage(Messages.ERROR + String.format("Nation %s does not exist", name));
            return;
        }
        if (NationManager.getInstance().getNationByPlayer(sender) != null) {
            sender.sendMessage(Messages.ERROR + "You must leave your nation before you can join another");
            return;
        }
        if (!nation.isOpen() && !nation.getInvitedPlayers().contains(sender.getUniqueId())) {
            sender.sendMessage("This nation requires invitation");
            for (NCPlayer np : nation.getPlayers().keySet()) {
                Player player = Bukkit.getPlayer(np.getPlayerID());
                if (player == null) {
                    continue;
                }
                player.sendMessage(String.format("%s tried to join your nation.", sender.getName()));
            }
            return;
        }
        if (nation.getPlayers().keySet().size() >= Settings.maxPlayersPerNation) {
            sender.sendMessage(Messages.ERROR + "Nation " + nation.getName() + " is full! Join another nation, or create your own.");
            return;
        }
        if (nation.addPlayer(PlayerManager.getInstance().getPlayer(sender.getUniqueId()))) {
            nation.getInvitedPlayers().remove(sender.getUniqueId());
            sender.sendMessage(String.format("You successfully joined %s", nation.getName()));
        } else {
            sender.sendMessage(String.format("You are already a member of %s", nation.getName()));
        }
        nation.saveToFile();
    }
}
