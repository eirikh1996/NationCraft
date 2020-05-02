package io.github.eirikh1996.nationcraft.core.listener;

import io.github.eirikh1996.nationcraft.api.events.EventListener;
import io.github.eirikh1996.nationcraft.api.events.block.BurnBlockEvent;
import io.github.eirikh1996.nationcraft.api.events.block.IgniteBlockEvent;
import io.github.eirikh1996.nationcraft.api.nation.Nation;

public class BlockListener {

    @EventListener
    public void onBlockBurn(BurnBlockEvent event) {
        Nation locN = event.getBlock().getLocation().getNation();
        if (locN == null) {
            return;
        }
        if (locN.fireSpreadAllowed()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventListener
    public void onBlockIgnite(IgniteBlockEvent event) {
        Nation locN = event.getBlock().getLocation().getNation();
        if (locN == null) {
            return;
        }
        if (locN.fireSpreadAllowed()) {
            return;
        }
        event.setCancelled(true);
    }
}
