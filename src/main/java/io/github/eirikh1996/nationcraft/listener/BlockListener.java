package io.github.eirikh1996.nationcraft.listener;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player p = event.getPlayer();
        Nation foundNation = NationManager.getInstance().getNationAt(event.getBlock().getLocation());
        Nation pNation = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
        if (foundNation == null){
            return;
        }
        if (p.hasPermission("nationcraft.territory.bypass")){
            return;
        }
        if (pNation != foundNation){
            event.setCancelled(true);
            String name = NationManager.getInstance().getColor(p,foundNation) + foundNation.getName() + ChatColor.RESET;
            p.sendMessage(String.format("%s does not allow you to build in their territory", name));
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){

    }
}
