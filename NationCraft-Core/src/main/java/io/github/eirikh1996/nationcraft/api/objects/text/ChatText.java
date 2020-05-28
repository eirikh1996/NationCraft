package io.github.eirikh1996.nationcraft.api.objects.text;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents chat text that can be sent to a player
 */
public class ChatText implements Comparable<ChatText> {
    private final List<ChatTextComponent> textComponents;

    private ChatText(List<ChatTextComponent> textComponents) {
        this.textComponents = textComponents;
    }

    public String json() {
        String json = "{";
        json += "\"text\":\"\",";
        json += "\"extra\":[";
        List<String> extras = new ArrayList<>();
        for (ChatTextComponent component : textComponents) {
            extras.add(component.json());
        }
        json += String.join(",", extras);
        json += "]}";
        return json;
    }

    /**
     * Gets the builder for the <code>ChatText</code>
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(@NotNull ChatText o) {
        return toString().compareTo(o.toString());
    }

    public static class Builder {
        final List<ChatTextComponent> textList = new ArrayList<>();
        protected Builder() {

        }

        public Builder addText(ChatTextComponent component) {
            textList.add(component);
            return this;
        }

        public Builder addText(ChatText text) {
            textList.addAll(text.textComponents);
            return this;
        }

        public Builder addText(String text) {
            textList.add(new ChatTextComponent(text));
            return this;
        }

        public Builder addText(TextColor color, String text) {
            textList.add(new ChatTextComponent(color, text));
            return this;
        }

        public Builder addText(String text, TextStyle... styles) {
            textList.add(new ChatTextComponent(text, styles));
            return this;
        }

        public Builder addText(TextColor color,  String text, TextStyle... styles) {
            textList.add(new ChatTextComponent(color, text, styles));
            return this;
        }

        public Builder addText(String text, ClickEvent clickEvent) {
            textList.add(new ChatTextComponent(text, clickEvent));
            return this;
        }

        public Builder addText(TextColor color, String text, ClickEvent clickEvent) {
            textList.add(new ChatTextComponent(color, text, clickEvent));
            return this;
        }

        public Builder addText(String text, ClickEvent clickEvent, TextStyle... styles) {
            textList.add(new ChatTextComponent(text, clickEvent, styles));
            return this;
        }

        public Builder addText(TextColor color, String text, ClickEvent clickEvent, TextStyle... styles) {
            textList.add(new ChatTextComponent(color, text, clickEvent, styles));
            return this;
        }

        public Builder addText(String text, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(text, hoverEvent));
            return this;
        }

        public Builder addText(TextColor color, String text, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(color, text, hoverEvent));
            return this;
        }

        public Builder addText(String text, HoverEvent hoverEvent, TextStyle... styles) {
            textList.add(new ChatTextComponent(text, hoverEvent, styles));
            return this;
        }

        public Builder addText(TextColor color, String text, HoverEvent hoverEvent, TextStyle... styles) {
            textList.add(new ChatTextComponent(color, text, hoverEvent, styles));
            return this;
        }

        public Builder addText(String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(text, clickEvent, hoverEvent));
            return this;
        }

        public Builder addText(TextColor color, String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(color, text, clickEvent, hoverEvent));
            return this;
        }

        public Builder addText(String text, ClickEvent clickEvent, HoverEvent hoverEvent, TextStyle... styles) {
            textList.add(new ChatTextComponent(text, clickEvent, hoverEvent, styles));
            return this;
        }

        public Builder addText(TextColor color, String text, ClickEvent clickEvent, HoverEvent hoverEvent, TextStyle... styles) {
            textList.add(new ChatTextComponent(color, text, clickEvent, hoverEvent, styles));
            return this;
        }

        public ChatText build() {
            return new ChatText(textList);
        }

        @Override
        public String toString() {

            String ret = "";
            for (ChatTextComponent component : textList) {
                if (component.getColor() != null) {
                    ret += component.getColor().toString();
                }
                if (component.getStyles() != null) {
                    ret += component.getStyles().toString();
                }
                ret += component.getText();
            }
            return ret;
        }
    }
}
