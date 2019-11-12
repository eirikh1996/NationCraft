package io.github.eirikh1996.nationcraft.settlement.siege;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SiegePlayerCheckTask extends SiegeTask {
    private boolean enemyPlayersInTownCenter = false;

    public SiegePlayerCheckTask(Siege siege) {
        super(siege);
    }


    @Override
    public void execute() {
        final Entity[] entitiesPresent = siege.getSettlement().getTownCenter().getChunk().getEntities();
        for (Entity entity : entitiesPresent){
            if (!(entity instanceof Player)){
                continue;
            }
            final Player player = (Player) entity;
            final Nation pNation = NationManager.getInstance().getNationByPlayer(player.getUniqueId());
            if (pNation != null && pNation.equals(siege.getAttacker())){
                enemyPlayersInTownCenter = true;
                break;
            }
        }
    }

    public boolean isEnemyPlayersInTownCenter() {
        return enemyPlayersInTownCenter;
    }
}
