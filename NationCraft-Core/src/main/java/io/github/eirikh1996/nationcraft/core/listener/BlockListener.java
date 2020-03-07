package io.github.eirikh1996.nationcraft.core.listener;

import io.github.eirikh1996.nationcraft.api.events.EventListener;
import io.github.eirikh1996.nationcraft.api.events.block.BurnBlockEvent;
import io.github.eirikh1996.nationcraft.api.nation.Nation;

public class BlockListener {

    @EventListener
    public void onBlockBurn(BurnBlockEvent event) {
        if (event.getBlock().getLocation().getNation() == null) {
            return;
        }
        Nation locN = event.getBlock().getLocation().getNation();
        if (locN.fireSpreadAllowed()) {
            return;
        }
        event.setCancelled(true);
    }
}
