package io.github.eirikh1996.nationcraft.bukkit.listener;

import com.earth2me.essentials.Essentials;
import io.github.eirikh1996.nationcraft.api.NationCraftAPI;
import io.github.eirikh1996.nationcraft.api.config.NationSettings;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.nation.Relation;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import io.github.eirikh1996.nationcraft.core.messages.Messages;
import io.github.eirikh1996.nationcraft.api.nation.Nation;
import io.github.eirikh1996.nationcraft.api.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.settlement.Settlement;
import io.github.eirikh1996.nationcraft.api.settlement.SettlementManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Collections;
import java.util.List;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event){
        PlayerManager mgr = PlayerManager.getInstance();
        if (!mgr.hasJoinedBefore(event.getPlayer().getUniqueId())){
            mgr.addPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
            return;
        }
        final NCPlayer player = mgr.getPlayer(event.getPlayer().getUniqueId());
        if (player == null){
            mgr.addPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
            return;
        } else if (player.getName() == event.getPlayer().getName()){
            return;
        }
        player.updateName(event.getPlayer().getName());
    }



    @EventHandler
    public void  onPlayerDeath(PlayerDeathEvent event){
        if (Settings.player.reducePowerInWorlds.contains(event.getEntity().getWorld().getName())){
            event.getEntity().sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You did not lose any strength due to the world you were in");
            return;
        }
        final NCPlayer player = PlayerManager.getInstance().getPlayer(event.getEntity().getUniqueId());
        double power = player.getPower();
        power += Settings.player.PowerPerDeath;
        player.setPower(power);
        event.getEntity().sendMessage(NATIONCRAFT_COMMAND_PREFIX + " Your power is now " + power + " / " + Settings.player.MaxPower);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){

        //TODO: Move to Core
        //if player enters nation territory
        NCLocation from = BukkitUtils.getInstance().bukkitToNCLoc(event.getFrom());
        NCLocation to = BukkitUtils.getInstance().bukkitToNCLoc(event.getTo());
        NCPlayer player = PlayerManager.getInstance().getPlayer(event.getPlayer());
        io.github.eirikh1996.nationcraft.api.events.player.PlayerMoveEvent pme = new io.github.eirikh1996.nationcraft.api.events.player.PlayerMoveEvent(player, from, to);
        NationCraftAPI.getInstance().callEvent(pme);
        event.setCancelled(pme.isCancelled());


    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        Nation pn = NationManager.getInstance().getNationByPlayer(player.getUniqueId());
        Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(player.getUniqueId());
            //Does the player have an active bed spawn location, send him there

            if (player.getBedSpawnLocation() == null) {

                if (ps != null) { //Else if the player is member of a settlement, send him to its town center
                    event.setRespawnLocation(BukkitUtils.getInstance().ncToBukkitLoc(ps.getTownCenter().getTeleportationPoint()));
                } else if (pn != null && pn.getCapital() != null) {//If not member of a settlement, but member of a nation, send him to the capital's towncenter
                    event.setRespawnLocation(BukkitUtils.getInstance().ncToBukkitLoc(pn.getCapital().getTownCenter().getTeleportationPoint()));
                }
            }

    }


    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        String command = event.getMessage().substring(1).toLowerCase();
        Nation pNation = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
        Nation nationAtTerritory = BukkitUtils.getInstance().bukkitToNCLoc(p.getLocation()).getNation();
        if (command.startsWith("home") && NationCraft.getInstance().getEssentialsPlugin() != null){
            String[] parts = command.split(" ");
            String homeName;
            try {
                homeName = parts[1];
            } catch (ArrayIndexOutOfBoundsException e){
                homeName = "home";
            }
            Essentials ess = NationCraft.getInstance().getEssentialsPlugin();
            Location home;
            try {
                home = ess.getUser(p.getUniqueId()).getHome(homeName);
            } catch (Exception e) {
                return;
            }

            Nation tp = BukkitUtils.getInstance().bukkitToNCLoc(home).getNation();
            if (tp != null && !tp.equals(pNation)){
                ess.getUser(p.getUniqueId()).getHomes().remove(homeName);
                p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Your home %s was removed as it was set in the territory of %s", homeName, tp.getName(pNation)));
            }
        }
        if (pNation != null) {
            Relation rel = pNation.getRelationTo(nationAtTerritory);
            List<String> forbiddenCommands = NationSettings.ForbiddenCommands.getOrDefault(rel, Collections.emptyList());
            if (forbiddenCommands.contains(command) && !p.hasPermission("nationcraft.territory.cmdbypass")) {
                event.setCancelled(true);
                p.sendMessage(Messages.ERROR + String.format("You are not allowed to use command /%s in %s territory!", command, rel.name().toLowerCase()));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Essentials ess = NationCraft.getInstance().getEssentialsPlugin();
        if (ess == null) {
            return;
        }
        Location dest = event.getTo();
        ess.getUser(event.getPlayer()).getHomes();
    }


}
