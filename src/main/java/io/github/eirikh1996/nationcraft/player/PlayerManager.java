package io.github.eirikh1996.nationcraft.player;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import io.github.eirikh1996.nationcraft.settlement.Settlement;
import io.github.eirikh1996.nationcraft.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class PlayerManager extends BukkitRunnable implements Iterable<NCPlayer> {
    private static Map<UUID, NCPlayer> players = new HashMap<>();
    private static PlayerManager instance;
    private static final File playersDir = new File(NationCraft.getInstance().getDataFolder(), "players");
    public static void initialize(){
        instance = new PlayerManager();

        if (!playersDir.exists()){
            return;
        }
        for (File playerFile : playersDir.listFiles()){
            if (playerFile == null || !playerFile.getName().endsWith(".player")){
                continue;
            }
            final NCPlayer ncPlayer = new NCPlayer(playerFile);
            players.put(ncPlayer.getPlayerID(), ncPlayer);
        }
        instance.runTaskTimerAsynchronously(NationCraft.getInstance(), 0, 20);
    }

    private PlayerManager(){
    }

    public boolean playerIsAtLeast(Player p, Ranks r){
        Nation n = NationManager.getInstance().getNationByPlayer(p.getUniqueId());
        boolean hasMinRank = false;
        if (n == null){
            return false;
        }
        Ranks testRank = n.getRankByPlayer(p);
        switch (r){
            case RECRUIT:
                hasMinRank = (testRank == Ranks.RECRUIT || testRank == Ranks.MEMBER || testRank == Ranks.OFFICER || testRank == Ranks.OFFICIAL ||  testRank == Ranks.LEADER);
                break;
            case MEMBER:
                hasMinRank = (testRank == Ranks.MEMBER || testRank == Ranks.OFFICER || testRank == Ranks.OFFICIAL ||  testRank == Ranks.LEADER);
                break;
            case OFFICER:
                hasMinRank = (testRank == Ranks.OFFICER || testRank == Ranks.OFFICIAL ||  testRank == Ranks.LEADER);
                break;
            case OFFICIAL:
                hasMinRank = (testRank == Ranks.OFFICIAL ||  testRank == Ranks.LEADER);
                break;
            case LEADER:
                hasMinRank = (testRank == Ranks.LEADER);
                break;

        }
        return hasMinRank;
    }
    public boolean hasJoinedBefore(UUID id){
        return players.containsKey(id);
    }

    public static PlayerManager getInstance() {
        return instance;
    }

    public Map<UUID, NCPlayer> getPlayers() {
        return players;
    }

    public void addPlayer(UUID id , String name){
        players.put(id, new NCPlayer(id, name));
    }

    @NotNull
    @Override
    public Iterator<NCPlayer> iterator() {
        return Collections.unmodifiableCollection(players.values()).iterator();
    }

    @Override
    public void run() {
        processInactivityRemoval();
        processPowerRegeneration();
    }

    public NCPlayer getPlayer(UUID uuid){
        return players.get(uuid);
    }



    private void processInactivityRemoval(){
        for (UUID id : players.keySet()){
            final NCPlayer ncPlayer = players.get(id);
            //Ignore if player is online
            if (Bukkit.getPlayer(ncPlayer.getPlayerID()) != null){
                continue;
            }
            long daysOfInactivity = TimeUtils.daysSince(ncPlayer.getLastActivityTime());
            if (daysOfInactivity <= Settings.PlayerMaxDaysInactivity){
                continue;
            }
            final Nation pNation = NationManager.getInstance().getNationByPlayer(id);
            if (pNation != null){
                NationCraft.getInstance().getLogger().info("Player " + ncPlayer.getName() + " removed after " + daysOfInactivity + " days of inactivity");
                pNation.removePlayer(id);
            }
            final Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(id);
            if (ps != null){
                ps.removePlayer(id);
            }
        }
    }

    private void processPowerRegeneration(){
        final double powerPerSecond = Settings.PlayerPowerPerHour / 3600.0;
        if (players.isEmpty()){
            return;
        }
        for (UUID id : players.keySet()){
            final NCPlayer player = players.get(id);
            if (player.getPower() >= Settings.PlayerMaxPower){
                continue;
            }
            if (!Settings.PlayerRegeneratePowerOffline && Bukkit.getPlayer(player.getPlayerID()) == null){
                continue;
            }
            double power = player.getPower();
            power += powerPerSecond;
            player.setPower(min(power, Settings.PlayerMaxPower));
        }
    }

}
