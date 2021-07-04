package io.github.eirikh1996.nationcraft.bukkit.listener;

import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.nation.Relation;
import io.github.eirikh1996.nationcraft.api.objects.text.TextColor;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
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
        Nation n = BukkitUtils.getInstance().bukkitToNCLoc(event.getLocation()).getNation();
        if (n != null && !n.monstersAllowed()){
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damagee = (Player) event.getEntity();
            NCPlayer ncDamager = PlayerManager.getInstance().getPlayer(damager);
            NCPlayer ncDamagee = PlayerManager.getInstance().getPlayer(damagee);
            Nation atDamagerLoc = BukkitUtils.getInstance().bukkitToNCLoc(damager.getLocation()).getNation();
            Nation atDamageeLoc = BukkitUtils.getInstance().bukkitToNCLoc(damagee.getLocation()).getNation();
            if (atDamageeLoc != null && !atDamageeLoc.pvpAllowed()) {
                damager.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("PvP is not permitted in the territory of %s", atDamageeLoc.getName(damager.getUniqueId())) + TextColor.RESET);
                event.setCancelled(true);
            }
            else if (atDamagerLoc != null && !atDamagerLoc.pvpAllowed()) {
                damager.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("You cannot damage other players from %s's territory since PvP is not permitted", atDamagerLoc.getName(damager.getUniqueId())) + TextColor.RESET);
                event.setCancelled(true);
            }
            else if(ncDamagee.hasNation() && ncDamager.hasNation()) {
                Relation rel = ncDamagee.getNation().getRelationTo(ncDamager.getNation());
                if (NationSettings.DisableDamageFor.contains(rel)) {
                    damager.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("You cannot harm players in %s nation", rel.name().toLowerCase()));
                    event.setCancelled(true);
                    return;
                }
            }
            if (atDamageeLoc == ncDamagee.getNation() && ncDamagee.hasNation()) {
                Relation rel = ncDamagee.getNation().getRelationTo(ncDamager.getNation());
                double percent = NationSettings.DamageReductionPercentage.getOrDefault(rel, 0.0);
                if (percent == 0.0) {
                    return;
                }
                double damage = event.getDamage();
                damage = damage - (damage * (percent / 100.0));
                damagee.sendMessage(NATIONCRAFT_COMMAND_PREFIX + String.format("%s damage reduced by %.2f percent", rel.capitalizedName(), percent));
                event.setDamage(damage);
            }

        }
    }
}
