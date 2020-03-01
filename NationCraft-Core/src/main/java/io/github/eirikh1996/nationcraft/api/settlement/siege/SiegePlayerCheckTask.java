package io.github.eirikh1996.nationcraft.api.settlement.siege;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;

public class SiegePlayerCheckTask extends SiegeTask {
    private boolean enemyPlayersInTownCenter = false;

    public SiegePlayerCheckTask(Siege siege) {
        super(siege);
    }


    @Override
    public void execute() {

    }

    public boolean isEnemyPlayersInTownCenter() {
        return enemyPlayersInTownCenter;
    }
}
