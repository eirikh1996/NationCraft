package io.github.eirikh1996.nationcraft.nation;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class NationSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Nation nation : NationManager.getInstance().getNations()) {
            if (!NationManager.getInstance().nationDataChanged(nation)) {
                return;
            }
            nation.saveNationToFile();
        }
    }
}
