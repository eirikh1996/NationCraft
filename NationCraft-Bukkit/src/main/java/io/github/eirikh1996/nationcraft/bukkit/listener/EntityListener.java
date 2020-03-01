package io.github.eirikh1996.nationcraft.bukkit.listener;

import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class EntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMonsterSpawn(EntitySpawnEvent event){
        if (!(event.getEntity() instanceof Monster)){
            return;
        }
        Nation n = NationManager.getInstance().getNationAt(BukkitUtils.getInstance().bukkitToNCLoc(event.getLocation()));
        if (n == null){
            return;
        } else if (!n.monstersAllowed()){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damagee = (Player) event.getEntity();
            Nation atDamagerLoc = NationManager.getInstance().getNationAt(BukkitUtils.getInstance().bukkitToNCLoc(damager.getLocation()));
            Nation atDamageeLoc = NationManager.getInstance().getNationAt(BukkitUtils.getInstance().bukkitToNCLoc(damagee.getLocation()));
            if (atDamageeLoc != null && !atDamageeLoc.pvpAllowed()) {
                damager.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("PvP is not permitted in the territory of %s", atDamageeLoc.getName(damager.getUniqueId())));
                event.setCancelled(true);
            }
            else if (atDamagerLoc != null && !atDamagerLoc.pvpAllowed()) {
                damager.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("You cannot damage other players from %s's territory since PvP is not permitted", atDamagerLoc.getName(damager.getUniqueId())));
                event.setCancelled(true);
            }
        }
    }
}
