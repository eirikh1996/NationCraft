package io.github.eirikh1996.nationcraft.settlement.siege;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class SiegeTask extends BukkitRunnable {
    protected final Settlement settlement;
    protected final Nation attacker;
    protected final Nation defender;

    protected SiegeTask(Settlement settlement, Nation attacker, Nation defender) {
        this.settlement = settlement;
        this.attacker = attacker;
        this.defender = defender;
    }

    @Override
    public void run() {
        execute();
    }
    public abstract void execute();
}
