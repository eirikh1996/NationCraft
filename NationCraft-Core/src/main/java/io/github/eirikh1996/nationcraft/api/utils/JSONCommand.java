package io.github.eirikh1996.nationcraft.api.utils;

public class JSONCommand {
    private String command;
    private Runnable event;

    public JSONCommand(String command, Runnable event) {
        this.command = command;
        this.event = event;
    }

    public String getCommand() {
        return this.command;
    }

    public Runnable getEvent() {
        return this.event;
    }
}
