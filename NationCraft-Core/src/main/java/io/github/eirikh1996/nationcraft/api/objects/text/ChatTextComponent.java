package io.github.eirikh1996.nationcraft.api.objects.text;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ChatTextComponent {

    @Nullable
    private ClickEvent clickEvent;
    @Nullable private HoverEvent hoverEvent;
    @Nullable private TextColor color;
    @Nullable private TextStyle style;
    @NotNull
    private final String text;

    public ChatTextComponent(@NotNull String text) {
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextStyle style, @NotNull String text) {
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @NotNull String text) {
        this.color = color;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @Nullable TextStyle style, @NotNull String text) {
        this.color = color;
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@NotNull String text, @Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextStyle style, @NotNull String text, @Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @NotNull String text, @Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        this.color = color;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @Nullable TextStyle style, @NotNull String text, @Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        this.color = color;
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@NotNull String text, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextStyle style, @NotNull String text, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @NotNull String text, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.color = color;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @Nullable TextStyle style, @NotNull String text, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.color = color;
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@NotNull String text, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextStyle style, @NotNull String text, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
        this.style = style;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @NotNull String text, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
        this.color = color;
        this.text = text;
    }

    public ChatTextComponent(@Nullable TextColor color, @Nullable TextStyle style, @NotNull String text, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
        this.color = color;
        this.style = style;
        this.text = text;
    }

    @Nullable
    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public void setClickEvent(@Nullable ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Nullable
    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }

    public void setHoverEvent(@Nullable HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
    }

    @Nullable
    public TextColor getColor() {
        return color;
    }

    public void setColor(@Nullable TextColor color) {
        this.color = color;
    }

    @Nullable
    public TextStyle getStyle() {
        return style;
    }

    public void setStyle(@Nullable TextStyle style) {
        this.style = style;
    }

    @NotNull
    public String getText() {
        return text;
    }

    public String json() {
        String json = "{";
        json += "\"text\":\"";
        json += text.replace("\\", "\\\\");
        json += "\"";
        if (style != null) {
            json += ",\"";
            json += style.name().toLowerCase();
            json += "\":\"true\",";
        }
        if (color != null) {
            json += ",\"";
            json += "color\":";
            json += "\"";
            json += color.name().toLowerCase();
            json += "\"";
        }
        if (clickEvent != null) {
            json += ",\"clickEvent\":{";
            json += "\"action\":";
            json += "\"";
            json += clickEvent.getAction().name().toLowerCase();
            json += "\",\"value\":\"";
            json += clickEvent.getValue();
            json += "\"}";
        }
        if (hoverEvent != null) {
            json += ",\"hoverEvent\":{";
            json += "\"action\":";
            json += "\"";
            json += hoverEvent.getAction().name().toLowerCase();
            json += "\",\"value\":\"";
            json += hoverEvent.getValue();
            json += "\"}";
        }
        json += "}";
        return json;
    }
}
