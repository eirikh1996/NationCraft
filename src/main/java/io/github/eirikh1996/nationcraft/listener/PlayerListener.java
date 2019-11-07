package io.github.eirikh1996.nationcraft.listener;

import com.earth2me.essentials.Essentials;
import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.messages.Messages;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Relation;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.territory.Territory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

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
        if (Settings.reducePowerInWorlds.contains(event.getEntity().getWorld().getName())){
            event.getEntity().sendMessage(NATIONCRAFT_COMMAND_PREFIX + "You did not lose any strength due to the world you were in");
            return;
        }
        final NCPlayer player = PlayerManager.getInstance().getPlayer(event.getEntity().getUniqueId());
        double power = player.getPower();
        power += Settings.reducePowerOnDeath;
        player.setPower(power);
        event.getEntity().sendMessage(NATIONCRAFT_COMMAND_PREFIX + " Your power is now " + power + " / " + Settings.maxPowerPerPlayer);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        //if player enters nation territory
        @Nullable Nation fromN = NationManager.getInstance().getNationAt(event.getFrom());
        @Nullable Nation toN = NationManager.getInstance().getNationAt(event.getTo());
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();
        final NCPlayer player = PlayerManager.getInstance().getPlayer(event.getPlayer().getUniqueId());
        if (player.isAutoUpdateTerritoryMap()) {

            if (fromChunk != toChunk) {
                Messages.generateTerritoryMap(event.getPlayer());
            }
        }
        Settlement fromS = SettlementManager.getInstance().getSettlementAt(event.getFrom());
        Settlement toS = SettlementManager.getInstance().getSettlementAt(event.getTo());
        if (toS != null || fromS != null){
            if (toS == null){
                event.getPlayer().sendMessage("Leaving the settlement of " + fromS.getName());
            } else if (fromS == null){
                event.getPlayer().sendMessage("Entering the settlement of " + toS.getName());
            } else if (toS.getTownCenter().equalsTerritory(Territory.fromChunk(toChunk)) && !toS.getTownCenter().equalsTerritory(Territory.fromChunk(fromChunk))){
                event.getPlayer().sendMessage("Entering the town center of " + toS.getName());
            } else if (fromS.getTownCenter().equalsTerritory(Territory.fromChunk(fromChunk)) && !fromS.getTownCenter().equalsTerritory(Territory.fromChunk(toChunk))){
                event.getPlayer().sendMessage("Leaving the town center of " + fromS.getName());
            }
        }
        if (fromN != toN){
            String fromNationName = (fromN != null ? NationManager.getInstance().getColor(event.getPlayer(), fromN) + fromN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
            String toNationName = (toN != null ? NationManager.getInstance().getColor(event.getPlayer(), toN) + toN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
            event.getPlayer().sendTitle(toNationName, toN == null ? "" : toN.getDescription(),10,70,20);
            event.getPlayer().sendMessage(String.format("Leaving %s, entering %s", fromNationName, toNationName));
        }

        //auto update map if player is moving


    }

    public void onPlayerQuit(PlayerQuitEvent event){
        Player p = event.getPlayer();
        if (p.isBanned()){
            //Remove banned players from their nation
            Nation pn = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
            if (pn != null) {
                pn.removePlayer(p.getUniqueId());
            }

        }
        PlayerManager.getInstance().getPlayer(p.getUniqueId()).setLastActivityTime(System.currentTimeMillis());

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        Nation pn = NationManager.getInstance().getNationByPlayer(player.getUniqueId());
        Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(player.getUniqueId());
            //Does the player have an active bed spawn location, send him there
        Bukkit.broadcastMessage(String.valueOf(player.getBedSpawnLocation()));

            if (player.getBedSpawnLocation() == null) {
                Bukkit.broadcastMessage(String.valueOf(pn));

                if (ps != null) { //Else if the player is member of a settlement, send him to its town center
                    Bukkit.broadcastMessage(String.valueOf(ps));
                    event.setRespawnLocation(ps.getTownCenter().getTeleportationPoint());
                } else if (pn != null && pn.getCapital() != null) {//If not member of a settlement, but member of a nation, send him to the capital's towncenter
                    event.setRespawnLocation(pn.getCapital().getTownCenter().getTeleportationPoint());
                }
            }

    }
    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        String command = event.getMessage().substring(1).toLowerCase();
        Nation pNation = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
        Nation nationAtTerritory = NationManager.getInstance().getNationAt(p.getLocation());
        if (command.startsWith("home") && NationCraft.getInstance().getEssentialsPlugin() != null){
            String[] parts = command.split(" ");
            String homeName;
            try {
                homeName = parts[1];
            } catch (ArrayIndexOutOfBoundsException e){
                homeName = "home";
            }
            Essentials ess = NationCraft.getInstance().getEssentialsPlugin();
            Location home = null;
            try {
                ess.getUser(p.getUniqueId()).getHome(homeName);
            } catch (Exception e) {
                return;
            }

            Nation tp = NationManager.getInstance().getNationAt(home);
            if (tp != null && !tp.equals(pNation)){
                ess.getUser(p.getUniqueId()).getHomes().remove(homeName);
                p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + String.format("Your home %s was removed as it was set in the territory of %s", homeName, tp.getName(pNation)));
            }
        }
        boolean isInEnemyTerritory = false;
        if (pNation != null)
                isInEnemyTerritory = pNation.getRelationTo(nationAtTerritory) == Relation.ENEMY;
        if (Settings.forbiddenCommandsInEnemyTerritory.contains(command) && isInEnemyTerritory && !p.hasPermission("nationcraft.territory.cmdbypass")){
            event.setCancelled(true);
            p.sendMessage(Messages.ERROR + String.format("You are not allowed to use command /%s in enemy territory!", command));
        }
    }


}
