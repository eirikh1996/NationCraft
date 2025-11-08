package io.github.eirikh1996.nationcraft.api.objects.text;

@Deprecated
public enum TextStyle {
    OBFUSCATED("§k"),
    BOLD("§l"),
    STRIKETHROUGH("§m"),
    UNDERLINE("§n"),
    ITALIC("§o"),
    ;

    private final String styleChar;

    TextStyle(String s) {
        this.styleChar = s;
    }

    public String getStyleChar() {
        return styleChar;
    }

    @Override
    public String toString() {
        return styleChar;
    }
}
