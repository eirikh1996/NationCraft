package io.github.eirikh1996.nationcraft.player;

import io.github.eirikh1996.nationcraft.NationCraft;
import io.github.eirikh1996.nationcraft.chat.ChatMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class PlayerManager implements Iterable<NCPlayer> {
    private static Map<UUID, Map<String, Object>> players;
    private static PlayerManager instance;
    private String FILE_PATH = NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/players.yml";
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
                    UUID id = UUID.fromString(entry.getKey());
                    Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                    ChatMode mode = ChatMode.valueOf((String) valueMap.get("chatMode"));
                    returnSet.put(id, valueMap);
                }
            }
        }
        return returnSet;
    }

    public void savePlayerDataToFile(){
        File playerFile = new File(FILE_PATH);
            try {
                FileWriter writer = new FileWriter(playerFile);
                writer.write("players: \n");
                for (UUID id : players.keySet()) {
                    Map<String, Object> valueMap = players.get(id);
                    writer.write("  " + id + ":\n");
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
        Map<String, Object> valueMap = players.get(p.getUniqueId().toString());
        return (ChatMode) valueMap.get("chatMode");
    }

    public static PlayerManager getInstance() {
        return instance;
    }

    public Map<UUID, Map<String, Object>> getPlayers() {
        return players;
    }

    @NotNull
    @Override
    public Iterator<NCPlayer> iterator() {
        return null;
    }
}
