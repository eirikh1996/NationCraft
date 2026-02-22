package io.github.eirikh1996.nationcraft.core.settlement.siege;

import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;

import java.util.HashSet;
import java.util.Set;

public class SiegePlayerCheckTask extends SiegeTask {
    private boolean enemyPlayersInTownCenter = false;

    public SiegePlayerCheckTask(Siege siege) {
        super(siege);
    }


    @Override
    public void execute() {
        final Nation attacker = siege.getAttacker();
        final Settlement settlement = siege.getSettlement();
        final Set<NCPlayer> enemyPlayersInTownCenter = new HashSet<>();
        for (NCPlayer player : attacker.getPlayers().keySet()) {
            if (!settlement.getTownCenter().contains(player.getLocation())) {
                continue;
            }
            enemyPlayersInTownCenter.add(player);
        }
        if (enemyPlayersInTownCenter.isEmpty()) {
            this.enemyPlayersInTownCenter = false;
            return;
        }
        this.enemyPlayersInTownCenter = true;
    }

    public boolean isEnemyPlayersInTownCenter() {
        return enemyPlayersInTownCenter;
    }
}
