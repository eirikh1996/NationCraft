package io.github.eirikh1996.nationcraft.api.objects.text;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a click event for a <code>ChatTextComponent</code>
 */
public class ClickEvent {

    @NotNull private final Action action;
    @NotNull private final String value;

    public ClickEvent(@NotNull Action action, @NotNull String value) {
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
        OPEN_URL, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE
    }
}
