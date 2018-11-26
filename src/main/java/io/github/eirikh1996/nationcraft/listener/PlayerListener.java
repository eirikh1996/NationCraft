package io.github.eirikh1996.nationcraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onLogin(PlayerLoginEvent event){

    }

    @EventHandler
    public void  onPlayerDeath(PlayerDeathEvent event){

    }
}
