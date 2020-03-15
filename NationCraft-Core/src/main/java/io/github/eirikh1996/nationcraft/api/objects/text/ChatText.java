package io.github.eirikh1996.nationcraft.api.objects.text;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

        public Builder addText(TextStyle style, String text) {
            textList.add(new ChatTextComponent(style, text));
            return this;
        }

        public Builder addText(TextColor color, TextStyle style, String text) {
            textList.add(new ChatTextComponent(color, style, text));
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

        public Builder addText(TextStyle style, String text, ClickEvent clickEvent) {
            textList.add(new ChatTextComponent(style, text, clickEvent));
            return this;
        }

        public Builder addText(TextColor color, TextStyle style, String text, ClickEvent clickEvent) {
            textList.add(new ChatTextComponent(color, style, text, clickEvent));
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

        public Builder addText(TextStyle style, String text, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(style, text, hoverEvent));
            return this;
        }

        public Builder addText(TextColor color, TextStyle style, String text, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(color, style, text, hoverEvent));
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

        public Builder addText(TextStyle style, String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(style, text, clickEvent, hoverEvent));
            return this;
        }

        public Builder addText(TextColor color, TextStyle style, String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
            textList.add(new ChatTextComponent(color, style, text, clickEvent, hoverEvent));
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
                if (component.getStyle() != null) {
                    ret += component.getStyle().toString();
                }
                ret += component.getText();
            }
            return ret;
        }
    }
}
