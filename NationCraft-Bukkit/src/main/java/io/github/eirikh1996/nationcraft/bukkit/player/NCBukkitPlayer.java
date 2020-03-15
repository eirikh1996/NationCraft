package io.github.eirikh1996.nationcraft.bukkit.player;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.api.player.NCPlayer;
import io.github.eirikh1996.nationcraft.api.utils.ReflectionUtils;
import io.github.eirikh1996.nationcraft.bukkit.NationCraft;
import io.github.eirikh1996.nationcraft.bukkit.utils.BukkitUtils;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static io.github.eirikh1996.nationcraft.api.utils.ReflectionUtils.*;

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
    public void sendMessage(@NotNull ChatText text) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null)
            return;
        Class<?> iChatBaseComponentClass;
        Class<?> chatSerializer;
        Class<?> packet;
        Object packetPlayOutChat;
        Object playerConnection;
        Object iChatBaseComponent;
        Object nmsPlayer;
        try {
            packet = ReflectionUtils.getClass(NMS_PACKAGE + "Packet");
            nmsPlayer = getMethod(p.getClass(), "getHandle").invoke(p);
            iChatBaseComponentClass = ReflectionUtils.getClass(NMS_PACKAGE + "IChatBaseComponent");
            chatSerializer = ReflectionUtils.getClass(NMS_PACKAGE + "IChatBaseComponent$ChatSerializer");
            iChatBaseComponent = ReflectionUtils.getMethod(chatSerializer, "a", String.class).invoke(null, text.json());
            packetPlayOutChat = ReflectionUtils.getClass(NMS_PACKAGE + "PacketPlayOutChat").getConstructor(iChatBaseComponentClass).newInstance(iChatBaseComponent);
            playerConnection = ReflectionUtils.getField(nmsPlayer.getClass(), "playerConnection").get(nmsPlayer);
            ReflectionUtils.getMethod(playerConnection.getClass(), "sendPacket", packet).invoke(playerConnection, packetPlayOutChat);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | InstantiationException | NoSuchFieldException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void sendActionBar(@NotNull String text) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null)
            return;
        final String jsonText = "{\"text\":\"" + text + "\"}";
        Class<?> iChatBaseComponentClass;
        Class<?> chatSerializer;
        Class<?> packet;
        Object packetPlayOutChat;
        Object playerConnection;
        Object iChatBaseComponent;
        Object nmsPlayer;
        try {
            packet = ReflectionUtils.getClass(NMS_PACKAGE + "Packet");
            nmsPlayer = getMethod(p.getClass(), "getHandle").invoke(p);
            iChatBaseComponentClass = ReflectionUtils.getClass(NMS_PACKAGE + "IChatBaseComponent");
            chatSerializer = ReflectionUtils.getClass(NMS_PACKAGE + "IChatBaseComponent$ChatSerializer");
            iChatBaseComponent = ReflectionUtils.getMethod(chatSerializer, "a", String.class).invoke(null, jsonText);
            if (Integer.parseInt(getServerVersion().split("_")[1]) >= 12) {
                Class<?> chatMessageType = ReflectionUtils.getClass(NMS_PACKAGE + "ChatMessageType");
                Object gameInfo = chatMessageType.getEnumConstants()[2];
                packetPlayOutChat = ReflectionUtils.getClass(NMS_PACKAGE + "PacketPlayOutChat").getConstructor(
                        iChatBaseComponentClass,
                        chatMessageType).newInstance(iChatBaseComponent, gameInfo);
            } else {
                packetPlayOutChat = ReflectionUtils.getClass(NMS_PACKAGE + "PacketPlayOutChat").getConstructor(
                        iChatBaseComponentClass,
                        byte.class
                ).newInstance(iChatBaseComponent, (byte) 2);
            }
            playerConnection = ReflectionUtils.getField(nmsPlayer.getClass(), "playerConnection").get(nmsPlayer);
            ReflectionUtils.getMethod(playerConnection.getClass(), "sendPacket", packet).invoke(playerConnection, packetPlayOutChat);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    public void sendTitle(String toNationName, String s, int i, int i1, int i2) {
        final Player p = Bukkit.getPlayer(playerID);
        if (p == null) {
            return;
        }
        p.sendTitle(toNationName, s, i, i1, i2);
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

    private String getServerVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }
}
