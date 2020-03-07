package io.github.eirikh1996.nationcraft.bukkit.player;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class NCBukkitPlayer extends NCPlayer {


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
    public NCLocation getLocation() {
        Player p = Bukkit.getPlayer(playerID);
        return p != null ? BukkitUtils.getInstance().bukkitToNCLoc(p.getLocation()) : null;
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
    public void sendMessage(String message) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            return;
        }
        p.sendMessage(message);
    }

    @Override
    public void sendMessage(String[] messages) {
        Bukkit.getPlayer(playerID).sendMessage(messages);
    }

    @Override
    public boolean hasPermission(String permission) {
        return Bukkit.getPlayer(playerID).hasPermission(permission);
    }
}
