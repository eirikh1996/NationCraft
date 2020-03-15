package io.github.eirikh1996.nationcraft.api.objects.text;

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

    @Override
    public String toString() {
        return colorChar;
    }
}
