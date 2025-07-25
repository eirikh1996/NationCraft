package io.github.eirikh1996.nationcraft.bukkit.player;

import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

import static io.github.eirikh1996.nationcraft.core.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.core.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class NCBukkitPlayer extends NCPlayer {

    private final String NMS_PACKAGE = "net.minecraft.server." + getServerVersion() + ".";


    public NCBukkitPlayer(UUID playerID, String name) {
        super(playerID, name);
    }

    public NCBukkitPlayer(UUID playerID, ChatMode chatMode) {
        super(playerID, chatMode);
    }

    public NCBukkitPlayer(File file) {
        super(file);
    }

    @Override
    public void sendMessage(@NotNull Component text) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null)
            throw new IllegalStateException("Player " + playerID + " is not online!");
        p.sendMessage(text);
    }

    @Override
    public void sendActionBar(@NotNull Component text) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null)
            throw new IllegalStateException("Player " + playerID + " is not online!");
        p.sendActionBar(text);

    }

    @Override
    public void teleport(NCLocation destination, String preTeleportMessage, String teleportMessage) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null)
            throw new IllegalStateException("Cannot teleport a player that is offline");
        final Location origin = p.getLocation();
        final Location bukkitDest = BukkitUtils.getInstance().ncToBukkitLoc(destination);
        int timePassed = (int) ((System.currentTimeMillis() - lastTeleportationTime) / 1000);
        if (timePassed <= Settings.player.TeleportationCooldown && !p.hasPermission("nationcraft.teleport.bypasscooldown")) {
            p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "You need to wait " + (Settings.player.TeleportationCooldown - timePassed) + " before you can teleport");
            return;
        }
        if (p.hasPermission("nationcraft.teleport.bypasswarmup")) {
            p.teleport(bukkitDest);
            lastTeleportationTime = System.currentTimeMillis();
            return;
        }
        p.sendMessage(preTeleportMessage);
        new BukkitRunnable() {
            int timePassed = 0;
            @Override
            public void run() {
                if (p.getLocation() != origin && !p.hasPermission("nationcraft.teleport.move")) {
                    p.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Teleport cancelled due to motion");
                    cancel();
                    return;
                }
                if (timePassed >= Settings.player.TeleportationWarmup) {
                    if (teleportMessage.length() > 0)
                        p.sendMessage(teleportMessage);
                    p.teleport(bukkitDest);
                    lastTeleportationTime = System.currentTimeMillis();
                    cancel();
                }
                timePassed++;
            }
        }.runTaskTimerAsynchronously(NationCraft.getInstance(), 0, 20);

    }

    @Override
    public NCLocation getLocation() {
        Player p = Bukkit.getPlayer(playerID);
        return p == null ? lastOnlineLocation : BukkitUtils.getInstance().bukkitToNCLoc(p.getLocation());
    }

    @Override
    public boolean isOnline() {
        return Bukkit.getPlayer(playerID) != null;
    }

    @Override
    public boolean charge(double fare) {
        //Return true and don't charge anything if no economy plugin is used
        final Economy eco = NationCraft.getInstance().getEconomy();
        if (eco == null) {
            return true;
        }
        OfflinePlayer op = Bukkit.getOfflinePlayer(playerID);
        if (!eco.has(op, fare)) {
            return false;
        }
        eco.withdrawPlayer(op, fare);
        return true;

    }

    @Override
    public void setLastOnlineLocation(NCLocation lastOnlineLocation) {

    }

    @Override
    public void sendTitle(String toNationName, String s, int i, int i1, int i2) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            throw new IllegalStateException("Player " + playerID + " is not online!");
        }
        p.sendTitle(toNationName, s, i, i1, i2);
    }

    @Override
    public void sendMessage(String message) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            throw new IllegalStateException("Player " + playerID + " is not online!");
        }
        p.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            throw new IllegalStateException("Player " + playerID + " is not online!");
        }
        p.sendMessage(messages);
    }

    @Override
    public boolean hasPermission(String permission) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            throw new IllegalStateException("Player " + playerID + " is not online!");
        }
        return p.hasPermission(permission);
    }

    private String getServerVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }
}
