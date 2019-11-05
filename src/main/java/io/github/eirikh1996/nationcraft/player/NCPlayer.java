package io.github.eirikh1996.nationcraft.player;


import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.config.Settings;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class NCPlayer {
    private final UUID playerID;
    private ChatMode chatMode;
    private double strength;
    private long lastActivityTime;
    private String name;
    private boolean autoUpdateTerritoryMap = false;
    private final List<String> previousNames = new ArrayList<>();
    public NCPlayer(UUID playerID, String name){
        this.name = name;
        this.playerID = playerID;
        chatMode = ChatMode.GLOBAL;
        strength = Settings.maxStrengthPerPlayer;
        lastActivityTime = System.currentTimeMillis();
        savetoFile();
    }
    public NCPlayer(UUID playerID, ChatMode chatMode){
        this.playerID = playerID;
        this.chatMode = chatMode;
        strength = Settings.maxStrengthPerPlayer;
        lastActivityTime = System.currentTimeMillis();
        savetoFile();
    }
    public NCPlayer(UUID playerID, ChatMode chatMode, int strength){
        this.playerID = playerID;
        this.chatMode = chatMode;
        this.strength = strength;
        lastActivityTime = System.currentTimeMillis();
        savetoFile();
    }
    public NCPlayer(File file){
        Yaml yaml = new Yaml();
        final Map data;
        try {
            data = (Map) yaml.load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new PlayerFileException("Something went wrong while loading player file", e);
        }
        name = (String) data.get("name");
        playerID = (UUID) data.get("playerID");
        chatMode = ChatMode.getChatMode((String) data.get("chatMode"));
        strength = (double) data.get("strength");
        lastActivityTime = (long) data.get("lastActivityTime");
        autoUpdateTerritoryMap = (boolean) data.get("autoUpdateTerritoryMap");
        final List<String> names = (List<String>) data.get("previousNames");
        previousNames.addAll(names != null ? names : Collections.emptyList());


    }

    public String getName() {
        return name;
    }

    public UUID getPlayerID(){
        return playerID;
    }

    public ChatMode getChatMode(){
        return chatMode;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
        savetoFile();
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
        savetoFile();
    }

    public boolean isAutoUpdateTerritoryMap() {
        return autoUpdateTerritoryMap;
    }

    public void setAutoUpdateTerritoryMap(boolean autoUpdateTerritoryMap) {
        this.autoUpdateTerritoryMap = autoUpdateTerritoryMap;
        savetoFile();
    }

    public void updateName(String name) {
        previousNames.add(getName().toLowerCase());
        this.name = name;
        savetoFile();

    }

    private void savetoFile() {
        final File playersDir = new File(NationCraft.getInstance().getDataFolder(), "players");
        final File playerFile = new File(playersDir, getPlayerID().toString() + ".player");
        try {
            if (!playersDir.exists()){
                playersDir.mkdirs();
            }
            if (!playerFile.exists()){
                playerFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream output;
        try {
            output = new FileOutputStream(playerFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        PrintWriter writer = new PrintWriter(output);
        writer.println("name: " + name);
        writer.println("playerID: " + playerID.toString());
        writer.println("chatMode: " + chatMode);
        writer.println("strength: " + strength);
        writer.println("lastActivityTime: " + lastActivityTime);
        writer.println("autoUpdateTerritoryMap: " + autoUpdateTerritoryMap);
        writer.println("previousNames:");
        for (String previousName : previousNames){
            writer.println("   - " + previousName);
        }
        writer.close();
    }

    public void setChatMode(ChatMode chatMode) {
        this.chatMode = chatMode;
    }

    private static class PlayerFileException extends RuntimeException {
        public PlayerFileException(String message, Throwable cause){
            super(message, cause);
        }
    }
}
