package io.github.eirikh1996.nationcraft.api.player;

import io.github.eirikh1996.nationcraft.api.NationCraftMain;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.config.WorldSettings;
import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.core.nation.Ranks;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.api.utils.TimeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class PlayerManager<P> implements Runnable, Iterable<NCPlayer> {
    protected static Map<UUID, NCPlayer> players = new HashMap<>();
    protected static PlayerManager<?> instance;
    protected static File playersDir;
    protected static NationCraftMain main;


    protected PlayerManager(){
    }

    public boolean playerIsAtLeast(NCPlayer p, Ranks r){
        Nation n = NationManager.getInstance().getNationByPlayer(p.getPlayerID());
        if (n == null){
            return false;
        }
        Ranks testRank = n.getRankByPlayer(p);
        return testRank.getPriority() <= r.getPriority();
    }
    public boolean hasJoinedBefore(UUID id){
        return players.containsKey(id);
    }

    public static PlayerManager<?> getInstance() {
        return instance;
    }

    public Map<UUID, NCPlayer> getPlayers() {
        return players;
    }

    public abstract void addPlayer(UUID id, String name);

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

    public abstract NCPlayer getPlayer(P player);

    @Nullable
    public NCPlayer getPlayer(String name) {
        for (NCPlayer player : players.values()) {
            if (!player.getName().equalsIgnoreCase(name))
                continue;
            return player;
        }
        return null;
    }



    private void processInactivityRemoval(){
        for (UUID id : players.keySet()){
            final NCPlayer ncPlayer = players.get(id);
            //Ignore if player is online
            if (ncPlayer.isOnline()){
                continue;
            }
            long daysOfInactivity = TimeUtils.daysSince(ncPlayer.getLastActivityTime());
            if (daysOfInactivity <= Settings.player.MaxDaysInactivity){
                continue;
            }
            final Nation pNation = NationManager.getInstance().getNationByPlayer(id);
            if (pNation != null){
                main.logInfo("Player " + ncPlayer.getName() + " removed after " + daysOfInactivity + " days of inactivity");
                pNation.removePlayer(ncPlayer);
            }
            final Settlement ps = SettlementManager.getInstance().getSettlementByPlayer(id);
            if (ps != null){
                ps.removePlayer(id);
            }
        }
    }

    private void processPowerRegeneration(){
        final double powerPerSecond = Settings.player.PowerPerHour / 3600.0;
        if (players.isEmpty()){
            return;
        }
        for (UUID id : players.keySet()){
            final NCPlayer player = players.get(id);
            if (player.getPower() >= Settings.player.MaxPower){
                continue;
            }
            if (!Settings.player.RegeneratePowerOffline && !player.isOnline()){
                continue;
            }
            if (!WorldSettings.powerGainEnabled.contains(player.getWorld())) {
                continue;
            }
            Nation locN = player.getLocation().getNation();
            if (locN != null && !locN.powergainAllowed()) {
                continue;
            }
            double power = player.getPower();
            power += powerPerSecond;
            player.setPower(Math.min(power, Settings.player.MaxPower));
        }
    }

    public File getPlayersDir() {
        return playersDir;
    }
}
