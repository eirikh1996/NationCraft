package io.github.eirikh1996.nationcraft.player;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    Map<String, Map<UUID, ?>> playerMap = new HashMap<>();
    public PlayerData(){
        this.getPlayerDataFromFile();
    }
    private void getPlayerDataFromFile(){
        File file = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/players.yml");
        if (!file.exists()){
            try {
                file.createNewFile();
                PrintWriter writer = new PrintWriter(file);
                writer.println("players:");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Map data;
        try {
            InputStream input = new FileInputStream(file);
            Yaml yaml = new Yaml();
            data = (Map) yaml.load(input);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }
    private Map<String, Map<UUID, ?>> playerMapFromObject(Object obj){
        HashMap<String, Map<UUID, ?>> returnMap = new HashMap<>();
        HashMap<Object,Object> objMap = (HashMap<Object,Object>) obj;
        for (Object o : objMap.keySet()){
            if (o instanceof String){
                objMap.get("players");

            }
        }
        return returnMap;
    }

    public Map<String, Map<UUID, ?>> getPlayerMap() {
        return playerMap;
    }




}
