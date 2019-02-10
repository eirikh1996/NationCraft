package io.github.eirikh1996.nationcraft.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Arrays;

public class EntityListener implements Listener {
    EntityType[] types = new EntityType[]{EntityType.ZOMBIE,
            EntityType.BLAZE,
            EntityType.SKELETON,
            EntityType.WITHER_SKELETON,
            EntityType.STRAY,
            EntityType.HUSK,
            EntityType.VINDICATOR,
            EntityType.EVOKER,
            EntityType.DROWNED,
            EntityType.PHANTOM};
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMonsterSpawn(EntitySpawnEvent event){
        if (Arrays.binarySearch(types, event.getEntityType()) < 0){
            return;
        }
    }
}
