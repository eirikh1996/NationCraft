package io.github.eirikh1996.nationcraft.core.chat;

import java.util.HashMap;
import java.util.Map;

public enum ChatMode {
    GLOBAL,
    ALLY,
    TRUCE,
    NATION,
    SETTLEMENT;
    private static final Map<String, ChatMode> BY_NAME = new HashMap<>();

    static {
        for (ChatMode mode : ChatMode.values()){
            BY_NAME.put(mode.name(), mode);
            BY_NAME.put(String.valueOf(mode.name().charAt(0)), mode);
        }
    }
    public static ChatMode getChatMode(String name){
        return BY_NAME.get(name);
    }

}