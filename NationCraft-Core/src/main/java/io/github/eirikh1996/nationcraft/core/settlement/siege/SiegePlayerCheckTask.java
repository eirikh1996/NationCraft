package io.github.eirikh1996.nationcraft.core.settlement.siege;

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
