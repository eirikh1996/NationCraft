package io.github.eirikh1996.nationcraft.player;


import io.github.eirikh1996.nationcraft.chat.ChatMode;
import io.github.eirikh1996.nationcraft.config.Settings;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NCPlayer {
    private UUID playerID;
    private ChatMode chatMode;
    private int strength;
    public NCPlayer(UUID playerID){
        this.playerID = playerID;
        chatMode = ChatMode.GLOBAL;
        strength = Settings.maxStrengthPerPlayer;
    }
    public NCPlayer(UUID playerID, ChatMode chatMode){
        this.playerID = playerID;
        this.chatMode = chatMode;
        strength = Settings.maxStrengthPerPlayer;
    }
    public NCPlayer(UUID playerID, ChatMode chatMode, int strength){
        this.playerID = playerID;
        this.chatMode = chatMode;
        this.strength = strength;
    }

    public UUID getPlayerID(){
        return playerID;
    }

    public ChatMode getChatMode(){
        return chatMode;
    }

    public int getStrength() {
        return strength;
    }
}
