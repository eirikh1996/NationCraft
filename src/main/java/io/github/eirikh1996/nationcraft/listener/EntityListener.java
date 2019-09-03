package io.github.eirikh1996.nationcraft.listener;

import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMonsterSpawn(EntitySpawnEvent event){
        if (!(event.getEntity() instanceof Monster)){
            return;
        }
        Nation n = NationManager.getInstance().getNationAt(event.getLocation());
        if (n == null){
            return;
        } else if (n.isSafezone()){
            event.setCancelled(true);
        }
    }
}
