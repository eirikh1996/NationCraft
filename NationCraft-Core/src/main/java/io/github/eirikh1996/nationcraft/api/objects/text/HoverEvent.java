package io.github.eirikh1996.nationcraft.api.objects.text;

public class HoverEvent {

    private final Action action;
    private final String value;

    public HoverEvent(Action action, String value) {
        this.action = action;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Action getAction() {
        return action;
    }


    public enum Action {
        SHOW_TEXT, SHOW_ENTITY, SHOW_ITEM;
    }
}
