package io.github.eirikh1996.nationcraft.api.player;

import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public abstract class NCPlayer implements NCCommandSender {
    protected final UUID playerID;
    protected ChatMode chatMode;
    protected double power;
    protected long lastActivityTime;
    protected String name;
    protected boolean autoUpdateTerritoryMap = false;
    protected int mapHeight = 20;
    protected boolean adminMode = false;
    protected final List<String> previousNames = new ArrayList<>();
    public NCPlayer(UUID playerID, String name){
        this.name = name;
        this.playerID = playerID;
        chatMode = ChatMode.GLOBAL;
        power = Settings.PlayerInitialPower;
        lastActivityTime = System.currentTimeMillis();
        savetoFile();
    }
    public NCPlayer(UUID playerID, ChatMode chatMode){
        this.playerID = playerID;
        this.chatMode = chatMode;
        power = Settings.PlayerInitialPower;
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
        playerID = UUID.fromString((String) data.get("playerID")) ;
        chatMode = ChatMode.getChatMode((String) data.get("chatMode"));
        power = (double) data.get("power");
        lastActivityTime = (long) data.get("lastActivityTime");
        autoUpdateTerritoryMap = (boolean) data.get("autoUpdateTerritoryMap");
        adminMode = (boolean) data.getOrDefault("adminMode", false);
        mapHeight = (int) data.getOrDefault("mapHeight", 20);
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

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
        savetoFile();
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public void setAdminMode(boolean adminMode) {
        this.adminMode = adminMode;
    }

    public long getLastActivityTime() {
        return lastActivityTime;
    }

    public void setLastActivityTime(long lastActivityTime) {
        this.lastActivityTime = lastActivityTime;
        savetoFile();
    }

    public abstract NCLocation getLocation();

    public abstract boolean isOnline();

    public boolean isAutoUpdateTerritoryMap() {
        return autoUpdateTerritoryMap;
    }

    public void setAutoUpdateTerritoryMap(boolean autoUpdateTerritoryMap) {
        this.autoUpdateTerritoryMap = autoUpdateTerritoryMap;
        savetoFile();
    }

    public void updateName(@NotNull String name) {
        if (name.equals(this.name)){
            return;
        }
        previousNames.add(getName().toLowerCase());
        this.name = name;
        savetoFile();
    }


    private void savetoFile() {
        final File playersDir = PlayerManager.getInstance().getPlayersDir();
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
        writer.println("power: " + power);
        writer.println("lastActivityTime: " + lastActivityTime);
        writer.println("autoUpdateTerritoryMap: " + autoUpdateTerritoryMap);
        writer.println("previousNames:");
        for (String previousName : previousNames){
            writer.println("   - " + previousName);
        }
        writer.close();
    }

    @Override
    public int hashCode() {
        return playerID.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NCPlayer)) {
            return false;
        }
        final NCPlayer np = (NCPlayer) obj;
        return playerID == np.playerID;
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
