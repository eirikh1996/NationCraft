package io.github.eirikh1996.nationcraft.api.objects.text;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a hover event for chat text components
 */
public class HoverEvent {

    @NotNull private final Action action;
    @NotNull private final String value;

    public HoverEvent(@NotNull Action action, @NotNull String value) {
        this.action = action;
        this.value = value;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    @NotNull
    public Action getAction() {
        return action;
    }


    public enum Action {
        SHOW_TEXT, SHOW_ENTITY, SHOW_ITEM;
    }
}
