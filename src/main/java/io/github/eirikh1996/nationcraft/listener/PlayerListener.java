package io.github.eirikh1996.nationcraft.listener;

import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.NCPlayer;
import io.github.eirikh1996.nationcraft.player.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event){
        PlayerManager mgr = PlayerManager.getInstance();
        if (!mgr.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            Map<String, Object> valueMap = new HashMap<>();
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
        Nation fromN = NationManager.getInstance().getNationAt(event.getFrom());
        Nation toN = NationManager.getInstance().getNationAt(event.getTo());
        NationManager manager = new NationManager();
        if (fromN == toN){
            return;
        }
        String fromNationName = (fromN != null ? NationManager.getInstance().getColor(event.getPlayer(), fromN) + fromN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
        String toNationName = (toN != null ? NationManager.getInstance().getColor(event.getPlayer(), toN) + toN.getName() : ChatColor.DARK_GREEN + "Wilderness") + ChatColor.RESET;
        event.getPlayer().sendMessage(String.format("Leaving %s, entering %s", fromNationName, toNationName));
    }
}
