package io.github.eirikh1996.nationcraft.core.settlement.siege;

import io.github.eirikh1996.nationcraft.core.Core;

public abstract class SiegeTask implements Runnable {
    protected final Siege siege;

    protected SiegeTask(Siege siege) {
        this.siege = siege;
    }

    @Override
    public void run() {
        try {
            execute();
            SiegeManager.getInstance().submitCompletedTask(this);
        } catch (Throwable t){
            Core.getMain().logError("Something went wrong while processing a siege on settlement " + siege.getSettlement() + " by " + siege.getAttacker().getName());
            throw new SiegeException(null, t);
        }

    }
    public abstract void execute();

    public Siege getSiege() {
        return siege;
    }
}
