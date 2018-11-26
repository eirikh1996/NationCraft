package io.github.eirikh1996.nationcraft.player;

import io.github.eirikh1996.nationcraft.NationCraft;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    Map<Player, Integer> playerMap = new HashMap<>();
    public PlayerData(){
        this.getPlayerDataFromFile();
    }
    private void getPlayerDataFromFile(){
        File file = new File(NationCraft.getInstance().getDataFolder().getAbsolutePath() + "/players.yml");
        if (!file.exists()){
            try {
                file.createNewFile();
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
        } finally {
            playerMap = playerMapFromObject("players");
        }
    }
    private Map<Player, Integer> playerMapFromObject(Object obj){
        HashMap<Player, Integer> returnMap = new HashMap<>();
        HashMap<Object,Object> objMap = (HashMap<Object,Object>) obj;
        for (Object o : objMap.keySet()){
            Player p = (Player) o;
            Object object = objMap.get(o);
            Integer strength = (Integer) object;
            returnMap.put(p, strength);
        }
        return returnMap;
    }

    public Map<Player, Integer> getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(Map<Player, Integer> playerMap) {
        this.playerMap = playerMap;
    }

    public void addPlayerToMap(Player player, Integer strength){
        this.playerMap.put(player, strength);
    }
}
