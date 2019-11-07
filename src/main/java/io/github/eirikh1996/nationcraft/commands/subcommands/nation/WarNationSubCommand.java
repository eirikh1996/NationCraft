package io.github.eirikh1996.nationcraft.commands.subcommands.nation;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class WarNationSubCommand extends NationSubCommand {
    private final String enemyName;

    public WarNationSubCommand(Player sender, String enemyName) {
        super(sender);
        this.enemyName = enemyName;
    }

    @Override
    public void execute() {
        Nation pNation = NationManager.getInstance().getNationByPlayer(sender.getUniqueId());
        Nation enemy = NationManager.getInstance().getNationByName(enemyName);
        if (pNation == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You are not in a nation");
            return;
        }
        else if (enemy == null){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Nation %s does not exist", enemyName));
            return;
        } else if (pNation.isAtWarWith(enemy)){
            sender.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Your nation is already at war with %s", enemyName));
            return;
        }
        if (pNation.getAllies().contains(enemy)){
            pNation.removeAlly(enemy);
        }
        pNation.addEnemy(enemy);
        for (NCPlayer np : pNation.getPlayers().keySet()){
            Player p = Bukkit.getPlayer(np.getPlayerID());
            if (p == null){
                continue;
            }
            p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("%s is now a hostile nation", enemy.getName(pNation)));
        }
        for (NCPlayer np : enemy.getPlayers().keySet()){
            Player p = Bukkit.getPlayer(np.getPlayerID());
            if (p == null){
                continue;
            }
            p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("%s is now a hostile nation", pNation.getName(enemy)));
        }
    }
}
