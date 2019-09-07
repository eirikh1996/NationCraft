package io.github.eirikh1996.nationcraft.player;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.config.Settings;
import io.github.eirikh1996.nationcraft.nation.Nation;
import io.github.eirikh1996.nationcraft.nation.NationManager;
import io.github.eirikh1996.nationcraft.nation.Ranks;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

import static io.github.eirikh1996.nationcraft.messages.Messages.ERROR;
import static io.github.eirikh1996.nationcraft.messages.Messages.NATIONCRAFT_COMMAND_PREFIX;

public class PlayerManager implements Iterable<NCPlayer> {
    private static Map<UUID, Map<String, Object>> players;
    private static PlayerManager instance;
    private String FILE_PATH = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/players.yml";
    private Map<UUID, Boolean> playerAutoMapUpdateEnabled = new HashMap<>();
    private Map<UUID, Location> playerTeleportationMap = new HashMap<>();
    private Map<UUID, TeleportCancellationReason> teleportCancellations = new HashMap<>();
    private Map<UUID, Long> lastTeleportMap = new HashMap<>();
    public static void initialize(){
        instance = new PlayerManager();
    }

    private PlayerManager(){
        players = loadPlayerList();
    }
    private Map<UUID, Map<String, Object>> loadPlayerList(){
        Map<UUID, Map<String, Object>> returnSet = new HashMap<>();
        File playerFile = new File(FILE_PATH);
        if (!playerFile.exists()){
            try {
                playerFile.createNewFile();
                PrintWriter writer = new PrintWriter(playerFile);
                writer.println("#WARNING: Editing this file may corrupt player data");
                writer.println("players:");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream input;
        try {
            input = new FileInputStream(playerFile);
        } catch (FileNotFoundException e) {
            input = null;
        }
        if (input != null) {
            Map data = new Yaml().loadAs(input, Map.class);
            @Nullable HashMap<String, HashMap<String, ?>> playerDataMap;
            try {
                playerDataMap = (HashMap<String, HashMap<String, ?>>) data.get("players");
            } catch (NullPointerException e){
                playerDataMap = null;
            }


            if (playerDataMap != null){
                for (Map.Entry<String, HashMap<String, ?>> entry : playerDataMap.entrySet()){
                    Player p;
                    Map<String, Object> dataMap = new HashMap<>();
                    UUID id = UUID.fromString(entry.getKey());
                    Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                    String name = (String) valueMap.get("name");
                    ChatMode mode = ChatMode.valueOf((String) valueMap.get("chatMode"));
                    int strength = (int) valueMap.get("strength");
                    dataMap.put("name", name);
                    dataMap.put("chatMode", mode);
                    dataMap.put("strength", strength);
                    returnSet.put(id, dataMap);
                }
            }
        }
        return returnSet;
    }
    public void scheduleTeleportation(final Player player, final Location tpLoc){
        if (lastTeleportMap.containsKey(player.getUniqueId()) && (System.currentTimeMillis() - lastTeleportMap.get(player.getUniqueId())) / 1000 < Settings.teleportationCooldownTime){
            int timePassed = (int) (System.currentTimeMillis() - lastTeleportMap.get(player.getUniqueId())) / 1000;
            int timeLeft = Settings.teleportationCooldownTime - timePassed;
            player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Time before the next teleport: " + (timeLeft > 0 ? (ChatColor.AQUA + "" + timeLeft + ChatColor.DARK_RED + " seconds") : "now"));
            return;
        }
        new BukkitRunnable() {
            private int timePassed = 0;
            @Override
            public void run() {
                timePassed++;
                if (teleportCancellations.containsKey(player.getUniqueId())){
                    player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + ERROR + "Teleportation cancelled due to " + teleportCancellations.get(player.getUniqueId()));
                    teleportCancellations.remove(player.getUniqueId());
                    cancel();
                }
                if (timePassed >= Settings.teleportationWarmupTime){
                    player.teleport(tpLoc);
                    lastTeleportMap.put(player.getUniqueId(), System.currentTimeMillis());
                    cancel();
                }
            }
        }.runTaskTimer(NationCraft.getInstance(), 0 , 20);
    }

    public void cancelTeleport(Player player, TeleportCancellationReason reason){
        teleportCancellations.put(player.getUniqueId(), reason);
    }

    public boolean playerIsAtLeast(Player p, Ranks r){
        Nation n = NationManager.getInstance().getNationByPlayer(p);
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
    public void savePlayerDataToFile(){
        File playerFile = new File(FILE_PATH);
            try {
                FileWriter writer = new FileWriter(playerFile);
                writer.write("players: \n");
                for (UUID id : players.keySet()) {
                    Map<String, Object> valueMap = players.get(id);
                    writer.write("  " + id + ":\n");
                    writer.write("    name: " + valueMap.get("name") + "\n");
                    writer.write("    chatMode: " + valueMap.get("chatMode") + "\n");
                    writer.write("    strength: " + valueMap.get("strength") + "\n");
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public ChatMode getChatModeByPlayer(Player p){
        Map<String, Object> valueMap = players.get(p.getUniqueId());
        return (ChatMode) valueMap.get("chatMode");
    }

    public void setChatMode(Player player, ChatMode chatMode){
        player.sendMessage(NATIONCRAFT_COMMAND_PREFIX + "Chat mode set: " + chatMode.name().toLowerCase());
        players.get(player.getUniqueId()).put("chatMode", chatMode);
    }
    public static PlayerManager getInstance() {
        return instance;
    }

    public Map<UUID, Map<String, Object>> getPlayers() {
        return players;
    }

    public UUID getPlayerIDFromName(String name){
        UUID returnID = null;
        for (Map.Entry<UUID, Map<String, Object>> entries : players.entrySet()){
            String foundName = (String) entries.getValue().get("name");
            if (foundName.equalsIgnoreCase(name)){
                returnID = entries.getKey();
            }
        }
        return returnID;
    }

    public int getPlayerStrength(UUID id){
        return (int) players.get(id).get("strength");
    }

    public boolean autoUpdateTerritoryMapOnMove(Player player){
        return playerAutoMapUpdateEnabled.get(player.getUniqueId()) != null ? playerAutoMapUpdateEnabled.get(player.getUniqueId()) : false;
    }

    public void setAutoUpdateTerritoryMapOnMove(Player player, boolean autoUpdate){
        playerAutoMapUpdateEnabled.put(player.getUniqueId(), autoUpdate);
    }

    @NotNull
    @Override
    public Iterator<NCPlayer> iterator() {
        return null;
    }

    public enum TeleportCancellationReason{
        TAKING_DAMAGE, MOTION
    }
}
