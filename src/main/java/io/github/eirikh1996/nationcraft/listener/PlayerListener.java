package io.github.eirikh1996.nationcraft.listener;

import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.player.PlayerData;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;



import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent event){

    }



    @EventHandler
    public void  onPlayerDeath(PlayerDeathEvent event){

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        //if player enters nation territory
        Chunk pChunk = event.getPlayer().getLocation().getChunk();
        NationManager manager = new NationManager();
        Set<Nation> nationSet = manager.getNations();
        for (Nation n : nationSet){
            List<Chunk> territory = n.getTerritory();
            if (territory.contains(pChunk)){
                event.getPlayer().sendMessage(n.getName() + " - " + n.getDescription());
            } else {
                event.getPlayer().sendMessage("Wilderness");
            }
        }

    }
}
