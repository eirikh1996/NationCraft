package io.github.eirikh1996.nationcraft.bukkit.player;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class BukkitPlayerManager extends PlayerManager<Player> implements Listener {
    private BukkitPlayerManager() {
        NationCraft.getInstance().getServer().getPluginManager().registerEvents(this, NationCraft.getInstance());
    }

    public static void initialize(NationCraftMain main){
        instance = new BukkitPlayerManager();
        playersDir  = new File(NationCraft.getInstance().getDataFolder(), "players");
        PlayerManager.main = main;
        if (!playersDir.exists()){
            return;
        }
        for (File playerFile : playersDir.listFiles()){
            if (playerFile == null || !playerFile.getName().endsWith(".player")){
                continue;
            }
            final NCPlayer ncPlayer = new NCBukkitPlayer(playerFile);
            players.put(ncPlayer.getPlayerID(), ncPlayer);
        }
        NationCraft.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(NationCraft.getInstance(), instance, 0, 20);

    }

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
        } else if (player.getName().equals(event.getPlayer().getName())){
            return;
        }
        player.updateName(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player p = event.getPlayer();
        getPlayer(p.getUniqueId()).setLastActivityTime(System.currentTimeMillis());
        getPlayer(p.getUniqueId()).setLastOnlineLocation(BukkitUtils.getInstance().bukkitToNCLoc(p.getLocation()));
    }

    @Override
    public void addPlayer(UUID id, String name) {
        players.put(id, new NCBukkitPlayer(id, name));
    }

    @Override
    public NCPlayer getPlayer(@NotNull Player player) {
        return getPlayer(player.getUniqueId());
    }

    public static BukkitPlayerManager getInstance() {
        return (BukkitPlayerManager) instance;
    }
}
