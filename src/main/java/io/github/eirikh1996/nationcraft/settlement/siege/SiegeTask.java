package io.github.eirikh1996.nationcraft.settlement.siege;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class SiegeTask extends BukkitRunnable {
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
            NationCraft.getInstance().getLogger().severe("Something went wrong while processing a siege on settlement " + siege.getSettlement() + " by " + siege.getAttacker().getName());
            throw new SiegeException(null, t);
        }

    }
    public abstract void execute();

    public Siege getSiege() {
        return siege;
    }
}
