package io.github.eirikh1996.nationcraft.bukkit.player;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.player.PlayerManager;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;

import java.io.File;
import java.util.UUID;

public class BukkitPlayerManager extends PlayerManager {
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


    @Override
    public void addPlayer(UUID id, String name) {
        players.put(id, new NCBukkitPlayer(id, name));
    }
}
