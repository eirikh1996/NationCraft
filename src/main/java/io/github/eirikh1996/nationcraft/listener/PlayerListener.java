package io.github.eirikh1996.nationcraft.listener;

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

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event){
        PlayerManager mgr = PlayerManager.getInstance();
        if (!mgr.getPlayers().containsKey(event.getPlayer().getUniqueId()) || mgr.getPlayers().get(event.getPlayer().getUniqueId()).get("name") != event.getPlayer().getName()){
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("name", event.getPlayer().getName());
            valueMap.put("chatMode", ChatMode.GLOBAL);
            valueMap.put("strength", Settings.maxStrengthPerPlayer);
            mgr.getPlayers().put(event.getPlayer().getUniqueId(), valueMap);
            mgr.savePlayerDataToFile();
        }
    }



    @EventHandler
    public void  onPlayerDeath(PlayerDeathEvent event){
        if (Settings.reduceStrengthInWorlds.contains(event.getEntity().getWorld().getName())){
            event.getEntity().sendMessage("You did not lose any strength due to the world you were in");
            return;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        //if player enters nation territory
        @Nullable Nation fromN = NationManager.getInstance().getNationAt(event.getFrom());
        @Nullable Nation toN = NationManager.getInstance().getNationAt(event.getTo());
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();
        if (PlayerManager.getInstance().autoUpdateTerritoryMapOnMove(event.getPlayer())) {

            if (fromChunk != toChunk) {
                Messages.generateTerritoryMap(event.getPlayer());
            }
        }
        Settlement fromS = null;
        Settlement toS = null;
        if (fromN != null){
            for (Settlement testS : fromN.getSettlements()){
                if (testS == null || !testS.getTerritory().contains(Territory.fromChunk(toChunk)) || testS.getTownCenter().equals(Territory.fromChunk(toChunk))){
                    continue;
                }
                fromS = testS;
            }
            if (fromN.getCapital() != null && fromN.getCapital().getTerritory().contains(Territory.fromChunk(toChunk))){
                fromS = fromN.getCapital();
            }
        }
        if (toN != null){
            for (Settlement testS : toN.getSettlements()){
                if (testS == null || !testS.getTerritory().contains(Territory.fromChunk(toChunk)) || testS.getTownCenter().equals(Territory.fromChunk(toChunk))){
                    continue;
                }
                toS = testS;
            }
            if (toN.getCapital() != null && toN.getCapital().getTerritory().contains(Territory.fromChunk(toChunk))){
                toS = toN.getCapital();
            }
        }
        if (toS != fromS){
            if (toS != null){
                event.getPlayer().sendTitle(toS.getName(), "");
                event.getPlayer().sendMessage("Now entering the settlement of " + toS.getName());
            } else {
                event.getPlayer().sendMessage("Now entering the settlement of " + fromS.getName());
            }
        } else if (toS != null && toS.getTownCenter().equals(Territory.fromChunk(toChunk))){
            event.getPlayer().sendMessage("Now entering the town center of " + toS.getName());
        } else if (fromS != null && fromS.getTownCenter().equals(Territory.fromChunk(toChunk))){
            event.getPlayer().sendMessage("Now leaving the town center of " + toS.getName());
        }
        if (fromN == toN){
            return;
        }
        String fromNationName = (fromN != null ? NationManager.getInstance().getColor(event.getPlayer(), fromN) + fromN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
        String toNationName = (toN != null ? NationManager.getInstance().getColor(event.getPlayer(), toN) + toN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
        event.getPlayer().sendTitle(toNationName, toN == null ? "" : toN.getDescription(),10,70,20);
        event.getPlayer().sendMessage(String.format("Leaving %s, entering %s", fromNationName, toNationName));
        //auto update map if player is moving


    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(player);
            //Does the player have an active bed spawn location, send him there
            if (player.getBedSpawnLocation() != null){
                return;
            } else if (ps != null){

            }


    }
    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        String command = event.getMessage().substring(1);
        Nation pNation = NationManager.getInstance().getNationByPlayer(p);
        Nation nationAtTerritory = NationManager.getInstance().getNationAt(p.getLocation());
        boolean isInEnemyTerritory = false;
        if (pNation != null)
                isInEnemyTerritory = pNation.getRelationTo(nationAtTerritory) == Relation.ENEMY;
        if (Settings.forbiddenCommandsInEnemyTerritory.contains(command) && isInEnemyTerritory && !p.hasPermission("nationcraft.territory.cmdbypass")){
            event.setCancelled(true);
            p.sendMessage(Messages.ERROR + String.format("You are not allowed to use command /%s in enemy territory!", command));
        }
    }


}
