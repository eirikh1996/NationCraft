package io.github.eirikh1996.nationcraft.api.objects.text;

public class ClickEvent {

    private final Action action;
    private final String value;

    public ClickEvent(Action action, String value) {
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
        OPEN_URL, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE
    }
}
