package io.github.eirikh1996.nationcraft.bukkit.listener;

import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.events.block.BurnBlockEvent;
import io.github.eirikh1996.nationcraft.api.events.block.IgniteBlockEvent;
import io.github.eirikh1996.nationcraft.api.events.block.LiquidFlowEvent;
import io.github.eirikh1996.nationcraft.api.events.world.TerrainEditEvent;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        TerrainEditEvent editEvent = new TerrainEditEvent(BukkitUtils.getInstance().bukkitToNCLoc(event.getBlock().getLocation()), TerrainEditEvent.Action.BREAK_BLOCK);
        NationCraftAPI.getInstance().callEvent(editEvent);
        event.setCancelled(editEvent.isCancelled());
        /*
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
        }*/
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        TerrainEditEvent editEvent = new TerrainEditEvent(BukkitUtils.getInstance().bukkitToNCLoc(event.getBlock().getLocation()), TerrainEditEvent.Action.PLACE_BLOCK);
        NationCraftAPI.getInstance().callEvent(editEvent);
        event.setCancelled(editEvent.isCancelled());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBurn(BlockBurnEvent event) {
        BurnBlockEvent e = new BurnBlockEvent(BukkitUtils.getInstance().getNCBlock(event.getIgnitingBlock()));
        NationCraftAPI.getInstance().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockIgnite(BlockIgniteEvent event) {
        IgniteBlockEvent e = new IgniteBlockEvent(BukkitUtils.getInstance().getNCBlock(event.getBlock()));
        NationCraftAPI.getInstance().callEvent(e);
        event.setCancelled(e.isCancelled());
    }

    @EventHandler
    public void onFlow(BlockFromToEvent event) {
        final LiquidFlowEvent nce = new LiquidFlowEvent(BukkitUtils.getInstance().getNCBlock(event.getBlock()), BukkitUtils.getInstance().getNCBlock(event.getToBlock()));
        NationCraftAPI.getInstance().callEvent(nce);
        event.setCancelled(nce.isCancelled());
    }
}
