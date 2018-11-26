package io.github.eirikh1996.nationcraft.listener;

import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;

public class PlayerListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        Player p = event.getPlayer();
        int maxStrength = Settings.maxStrengthPerPlayer;
        PlayerData data = new PlayerData();
        if (!data.getPlayerMap().containsKey(p)){
            data.addPlayerToMap(p, maxStrength);
        }
    }



    @EventHandler
    public void  onPlayerDeath(PlayerDeathEvent event){
        Player p = event.getEntity().getPlayer();
        PlayerData data = new PlayerData();
        Map<Player, Integer> pMap = data.getPlayerMap();
        if (pMap.containsKey(p)){
            Integer currentStrength = pMap.get(p);
            Integer strengthDecrement = Settings.reduceStrengthOnDeath;
            Integer reducedStrength = currentStrength - strengthDecrement;
            pMap.put(p,reducedStrength);
            data.setPlayerMap(pMap);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){

    }
}
