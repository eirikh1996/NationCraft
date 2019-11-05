package io.github.eirikh1996.nationcraft.chat;

import com.google.common.collect.Maps;

import java.util.Calendar;
import java.util.Map;

public enum ChatMode {
    GLOBAL,
    ALLY,
    TRUCE,
    NATION,
    SETTLEMENT;
    private static final Map<String, ChatMode> BY_NAME = Maps.newHashMapWithExpectedSize(ChatMode.values().length);

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