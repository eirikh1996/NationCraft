package io.github.eirikh1996.nationcraft.api.objects.text;

import java.util.HashMap;
import java.util.Map;

public enum  TextColor {
    BLACK("§0"),
    DARK_BLUE("§1"),
    DARK_GREEN("§2"),
    DARK_AQUA("§3"),
    DARK_RED("§4"),
    DARK_PURPLE("§5"),
    GOLD("§6"),
    GRAY("§7"),
    DARK_GRAY("§8"),
    BLUE("§9"),
    GREEN("§a"),
    AQUA("§b"),
    RED("§c"),
    LIGHT_PURPLE("§d"),
    YELLOW("§e"),
    WHITE("§f"),
    RESET("§r");

    private static final Map<String, TextColor> BY_NAME = new HashMap<>();

    static {
        for (TextColor color : values()) {
            BY_NAME.put(color.name(), color);
        }
    }

    private final String colorChar;

    TextColor(String colorChar) {
        this.colorChar = colorChar;
    }

    public static String strip(String str) {
        for (TextColor color : values()) {
            str = str.replace(color.toString(), "");
        }
        return str;
    }

    public static TextColor getColor(String str) {
        return BY_NAME.get(str);
    }

    public static TextColor getColorIgnoreCase(String str) {
        return getColor(str.toUpperCase());
    }

    @Override
    public String toString() {
        return colorChar;
    }
}
