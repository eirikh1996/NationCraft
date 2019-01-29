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
        if (fromN == toN){
            return;
        }
        String fromNationName = (fromN != null ? NationManager.getInstance().getColor(event.getPlayer(), fromN) + fromN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
        String toNationName = (toN != null ? NationManager.getInstance().getColor(event.getPlayer(), toN) + toN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
        event.getPlayer().sendMessage(String.format("Leaving %s, entering %s", fromNationName, toNationName));
        //auto update map if player is moving
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();
        if (PlayerManager.getInstance().getPlayerAutoMapUpdateEnabled() != null) {
            if (PlayerManager.getInstance().getPlayerAutoMapUpdateEnabled().get(event.getPlayer().getUniqueId())) {
                if (fromChunk != toChunk) {
                    Messages.generateTerritoryMap(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        for (Settlement s : SettlementManager.getInstance().getSettlements()){
            //Does the player have an active bed spawn location, send him there
            if (player.getBedSpawnLocation() != null){
                return;
            }
            
            if (s.getPlayers().contains(player.getUniqueId())){

            }
        }
    }
    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        String command = event.getMessage().substring(1);
        Nation pNation = NationManager.getInstance().getNationByPlayer(p);
        Nation nationAtTerritory = NationManager.getInstance().getNationAt(p.getLocation());
        boolean isInEnemyTerritory = pNation.getRelationTo(nationAtTerritory) == Relation.ENEMY;
        NationCraft.getInstance().getLogger().info(command);
        if (Settings.forbiddenCommandsInEnemyTerritory.contains(command) && isInEnemyTerritory && !p.hasPermission("nationcraft.territory.cmdbypass")){
            event.setCancelled(true);
            p.sendMessage(Messages.ERROR + String.format("You are not allowed to use command /%s in enemy territory!", command));
        }
    }
}
