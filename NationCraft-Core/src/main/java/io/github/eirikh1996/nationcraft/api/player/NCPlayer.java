package io.github.eirikh1996.nationcraft.api.player;

import io.github.eirikh1996.nationcraft.core.nation.Nation;
import io.github.eirikh1996.nationcraft.core.nation.NationManager;
import io.github.eirikh1996.nationcraft.api.objects.NCLocation;
import io.github.eirikh1996.nationcraft.api.objects.text.ChatText;
import io.github.eirikh1996.nationcraft.core.settlement.Settlement;
import io.github.eirikh1996.nationcraft.core.settlement.SettlementManager;
import io.github.eirikh1996.nationcraft.core.chat.ChatMode;
import io.github.eirikh1996.nationcraft.api.config.Settings;
import io.github.eirikh1996.nationcraft.core.commands.NCCommandSender;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public abstract class NCPlayer implements NCCommandSender {
    protected final UUID playerID;
    protected ChatMode chatMode;
    protected double power;
    protected long lastActivityTime;
    protected long lastMapUpdateTime;
    protected long lastTeleportationTime;
    protected String name;
    protected boolean autoUpdateTerritoryMap = false;
    protected int mapHeight = 20;
    protected boolean adminMode = false;
    protected final List<String> previousNames = new ArrayList<>();
    protected NCLocation lastOnlineLocation;
    public NCPlayer(UUID playerID, String name){
        this.name = name;
        this.playerID = playerID;
        chatMode = ChatMode.GLOBAL;
        power = Settings.player.InitialPower;
        lastActivityTime = System.currentTimeMillis();
        savetoFile();
    }
    public NCPlayer(UUID playerID, ChatMode chatMode){
        this.playerID = playerID;
        this.chatMode = chatMode;
        power = Settings.player.InitialPower;
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

    public abstract void sendMessage(@NotNull final Component text);

    @Deprecated
    public void sendActionBar(@NotNull final String text) {
        sendActionBar(Component.text(text));
    }

    public abstract void sendActionBar(@NotNull Component text);

    /**
     *
     * Teleports a player to a given location and sends a message when teleportation commences
     *
     * @param destination The location the player will be teleported to
     * @param teleportMessage the message to be sent to a player when teleportation commences
     */
    public abstract void teleport(NCLocation destination, String preTeleportMessage, String teleportMessage);

    public abstract NCLocation getLocation();

    public abstract boolean isOnline();

    /**
     * Charges the player a fare
     * @param fare The fare to charge the player
     * @return true if the player has sufficient funds. False otherwise
     */
    public abstract boolean charge(double fare);

    public void teleport(NCLocation destination) {
        teleport(destination, "", "");
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

    public Nation getNation() {
        return NationManager.getInstance().getNationByPlayer(this);
    }

    public boolean hasNation() {
        return getNation() != null;
    }

    public Settlement getSettlement() {
        return SettlementManager.getInstance().getSettlementByPlayer(this);
    }

    public boolean hasSettlement() {
        return getSettlement() != null;
    }

    public boolean isInNationWith(NCPlayer other) {
        if (hasNation()) {
            return getNation().equals(other.getNation());
        }
        return !other.hasNation();
    }

    public boolean isInSettlementWith(NCPlayer other) {
        return hasSettlement() && getSettlement().equals(other.getSettlement());
    }

    public boolean isAlliedWith(NCPlayer other) {
        return hasNation() && getNation().isAlliedWith(other.getNation());
    }

    public boolean isTrucedWith(NCPlayer other) {
        return hasNation() && getNation().isTrucedWith(other.getNation());
    }

    public boolean isAtWarWith(NCPlayer other) {
        return hasNation() && getNation().isAtWarWith(other.getNation());
    }

    public String getWorld() {
        return getLocation().getWorld();
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

    public long getLastMapUpdateTime() {
        return lastMapUpdateTime;
    }

    public void setLastMapUpdateTime(long lastMapUpdateTime) {
        this.lastMapUpdateTime = lastMapUpdateTime;
    }

    public void setLastOnlineLocation(NCLocation lastOnlineLocation) {
        this.lastOnlineLocation = lastOnlineLocation;
    }

    public abstract void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut);

    private static class PlayerFileException extends RuntimeException {
        public PlayerFileException(String message, Throwable cause){
            super(message, cause);
        }
    }
}
